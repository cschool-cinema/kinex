package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.*;

@Service
@AllArgsConstructor
public class ScreeningService {

    private static final short BREAK_AFTER_SCREENING = 15;

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;

    public List<Screening> findScreeningsStartingFrom(LocalDateTime from) {
        return screeningRepository
                .findAllByScreeningStartUtcGreaterThanEqualOrderByScreeningStartUtc(from);
    }

    public List<Screening> findAllScreenings() {
        return screeningRepository.findAll(Sort.by(Sort.Direction.ASC, "screeningStartUtc"));
    }

    public Screening findScreeningById(int id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));
    }

    public List<Screening> findScreeningsByMovieTitleAndStartTime(String title, LocalDateTime from) {
        return screeningRepository.findByMovieTitleAndStartTime(title, from);
    }

    @Transactional
    public String deleteScreening(int id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        if (screening.getTickets().size() > 0) {
            throw new NotAllowedException("Delete not allowed!" + TICKETS_SOLD);
        }

        screeningRepository.delete(screening);

        return "Screening has been successfully deleted.";
    }

    @Transactional
    public Screening createScreening(ScreeningRequestDto screeningRequestDto) {
        LocalDateTime screeningStart = screeningRequestDto.getScreeningStartUtc();

        if (screeningStart.isBefore(LocalDateTime.now())) {
            throw new NotAllowedException("Adding screenings in the past not allowed!");
        }

        Movie movie = movieRepository.findById(screeningRequestDto.getMovieId())
                .orElseThrow(() -> new NotFoundException(MOVIE_NOT_FOUND));

        Auditorium auditorium = auditoriumRepository.findById(screeningRequestDto.getAuditoriumId())
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

        LocalDateTime screeningEnd = screeningStart
                .plusMinutes(movie.getDurationMin())
                .plusMinutes(BREAK_AFTER_SCREENING);

        if (screeningRepository.screeningConflicts(screeningStart, screeningEnd, auditorium.getId()) > 0) {
            throw new NotAllowedException("There are other screenings reserved in this time window! " +
                    "Please choose different time or auditorium.");
        }

        Screening newScreening = Screening.builder()
                .auditorium(auditorium)
                .movie(movie)
                .screeningStartUtc(screeningStart)
                .build();

        return screeningRepository.save(newScreening);
    }

    @Transactional
    public Screening updateScreeningDetails(ScreeningRequestDto requestDto, int id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        boolean ticketsAlreadySold = activeTicketsSold(screening);

        LocalDateTime updateScreeningStart = requestDto.getScreeningStartUtc();

        if (updateScreeningStart.isBefore(LocalDateTime.now())) {
            throw new NotAllowedException("Cannot update screening date/time to past!");
        }

        if (screening.getScreeningStartUtc().isBefore(LocalDateTime.now())) {
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
            screening.setAuditorium(auditoriumRepository.findById(requestDto.getAuditoriumId())
                    .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND)));
        }

        if (!requestDto.getScreeningStartUtc().equals(screening.getScreeningStartUtc())) {
            if (ticketsAlreadySold) {
                throw new NotAllowedException("Date/time update not allowed!" + TICKETS_SOLD);
            }

            LocalDateTime updateScreeningEnd = updateScreeningStart
                    .plusMinutes(screening.getMovie().getDurationMin())
                    .plusMinutes(BREAK_AFTER_SCREENING);

            if (screeningRepository.screeningConflictsExcludingId(
                    updateScreeningStart, updateScreeningEnd, screening.getAuditorium().getId(), id) > 0) {
                throw new NotAllowedException("There are other screenings reserved in this time window! " +
                        "Please choose different time or auditorium.");
            }

            screening.setScreeningStartUtc(updateScreeningStart);
        }

        return screeningRepository.save(screening);
    }

    private boolean activeTicketsSold(Screening screening) {
        List<Ticket> tickets = screening.getTickets();

        if (tickets.size() == 0) {
            return false;
        }

        for (Ticket ticket : tickets) {
            if (ticket.isActive()) {
                return true;
            }
        }

        return false;
    }
}