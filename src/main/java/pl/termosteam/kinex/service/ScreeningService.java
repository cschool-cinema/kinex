package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.dto.ScreeningRequestDto;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.AuditoriumRepository;
import pl.termosteam.kinex.repository.MovieRepository;
import pl.termosteam.kinex.repository.ScreeningRepository;

import java.time.LocalDateTime;
import java.util.List;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.*;

@Service
@AllArgsConstructor
@Transactional
public class ScreeningService {

    @Value("${business.break-after-screening}")
    private static int breakMinutesAfterScreening;

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;

    public List<Screening> findScreeningsStartingFrom(LocalDateTime from) {
        return screeningRepository
                .findAllByScreeningStartGreaterThanEqualOrderByScreeningStart(from);
    }

    public List<Screening> findAllScreenings() {
        return screeningRepository.findAll(Sort.by(Sort.Direction.ASC, "screeningStart"));
    }

    public Screening findScreeningById(int id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));
    }

    public List<Screening> findFutureScreeningsByMovieTitle(String title) {
        return screeningRepository.findByMovieTitleAndStartTime(title, LocalDateTime.now());
    }

    public String deleteScreening(int id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        if (CollectionUtils.isNotEmpty(screening.getTickets())) {
            throw new NotAllowedException("Delete not allowed!" + TICKETS_SOLD);
        }

        screeningRepository.delete(screening);

        return "Screening has been successfully deleted.";
    }

    public Screening createScreening(ScreeningRequestDto screeningRequestDto) {
        LocalDateTime screeningStart = screeningRequestDto.getScreeningStart();

        Movie movie = movieRepository.findById(screeningRequestDto.getMovieId())
                .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND));

        Auditorium auditorium = auditoriumRepository.findById(screeningRequestDto.getAuditoriumId())
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

        if (!auditorium.getActive()) {
            throw new NotAllowedException("Cannot create a screening in an inactive auditorium!");
        }

        LocalDateTime screeningEnd = screeningStart
                .plusMinutes(movie.getDurationMin())
                .plusMinutes(breakMinutesAfterScreening);

        if (screeningRepository.screeningConflicts(screeningStart, screeningEnd, auditorium.getId()) > 0) {
            throw new NotAllowedException("There are other screenings reserved in this time window! " +
                    "Please choose different time or auditorium.");
        }

        Screening newScreening = Screening.builder()
                .auditorium(auditorium)
                .movie(movie)
                .screeningStart(screeningStart)
                .build();

        return screeningRepository.save(newScreening);
    }

    public Screening updateScreeningDetails(ScreeningRequestDto requestDto, int id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        boolean ticketsAlreadySold = screening.getTickets().stream()
                .anyMatch(Ticket::getActive);

        LocalDateTime updateScreeningStart = requestDto.getScreeningStart();

        if (screening.getScreeningStart().isBefore(LocalDateTime.now())) {
            throw new NotAllowedException("Cannot update past screening!");
        }

        if (requestDto.getMovieId() != screening.getMovie().getId()) {
            if (ticketsAlreadySold) {
                throw new NotAllowedException("Movie update not allowed!" + TICKETS_SOLD);
            }

            screening.setMovie(movieRepository.findById(requestDto.getMovieId())
                    .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND)));
        }

        if (requestDto.getAuditoriumId() != screening.getAuditorium().getId()) {
            Auditorium auditorium = auditoriumRepository.findById(requestDto.getAuditoriumId())
                    .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

            if (!auditorium.getActive()) {
                throw new NotAllowedException("Cannot update auditorium to an inactive one!");
            }

            screening.setAuditorium(auditorium);
        }

        if (!requestDto.getScreeningStart().equals(screening.getScreeningStart())) {
            if (ticketsAlreadySold) {
                throw new NotAllowedException("Date/time update not allowed!" + TICKETS_SOLD);
            }

            LocalDateTime updateScreeningEnd = updateScreeningStart
                    .plusMinutes(screening.getMovie().getDurationMin())
                    .plusMinutes(breakMinutesAfterScreening);

            if (screeningRepository.screeningConflictsExcludingId(
                    updateScreeningStart, updateScreeningEnd, screening.getAuditorium().getId(), id) > 0) {
                throw new NotAllowedException("There are other screenings reserved in this time window! " +
                        "Please choose different time or auditorium.");
            }

            screening.setScreeningStart(updateScreeningStart);
        }

        return screeningRepository.save(screening);
    }
}