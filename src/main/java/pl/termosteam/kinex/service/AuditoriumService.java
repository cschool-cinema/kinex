package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.AuditoriumRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.AUDITORIUM_NOT_FOUND;

@Service
@AllArgsConstructor
@Transactional
public class AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;

    public List<Auditorium> findAllAuditoriums() {
        return auditoriumRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Auditorium findAuditoriumById(int id) {
        return auditoriumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));
    }

    public Auditorium createAuditorium(Auditorium auditorium) {
        if (auditoriumRepository.existsByNameIgnoreCase(auditorium.getName())) {
            throw new NotAllowedException("Auditorium with this name already exists!");
        }

        List<Seat> seats = auditorium.getSeats();
        if (CollectionUtils.isEmpty(seats)) {
            throw new NotAllowedException("Please add at least one seat to auditorium!");
        }

        Set<String> seatUniqueRowAndNumber = seats.stream()
                .map(s -> s.getSeatRow().toString() + s.getSeatNumber().toString())
                .collect(Collectors.toSet());

        if (seats.size() != seatUniqueRowAndNumber.size()) {
            throw new NotAllowedException("Cannot add auditorium with duplicated seats (row, number)!");
        }

        boolean auditoriumActive = auditorium.getActive();
        boolean atLeastOneActiveSeat = false;

        for (Seat seat : seats) {
            seat.setAuditorium(auditorium);

            if (auditoriumActive && seat.getActive()) {
                atLeastOneActiveSeat = true;
            }
        }

        if (auditoriumActive && !atLeastOneActiveSeat) {
            throw new NotAllowedException("Cannot add active auditorium with all inactive seats!");
        }

        return auditoriumRepository.save(auditorium);
    }

    public String deactivateAuditorium(int auditoriumId) {
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

        if (!auditorium.getActive()) {
            throw new NotAllowedException("The auditorium is already inactive.");
        }

        if (auditoriumRepository.existsScreeningsStartingFrom(auditoriumId, LocalDateTime.now())) {
            throw new NotAllowedException("Cannot deactivate! Future screenings exist for this auditorium.");
        }

        List<Seat> seats = auditorium.getSeats();
        for (Seat seat : seats) {
            seat.setActive(false);
        }

        auditorium.setActive(false);

        auditoriumRepository.save(auditorium);

        return "The auditorium and all its seats have been deactivated.";
    }

    public String reactivateAuditorium(int auditoriumId) {
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

        if (auditorium.getActive()) {
            throw new NotAllowedException("Auditorium is already active.");
        }

        List<Seat> seats = auditorium.getSeats();
        for (Seat seat : seats) {
            seat.setActive(true);
        }

        auditorium.setActive(true);

        auditoriumRepository.save(auditorium);

        return "The auditorium and all its seats have been reactivated.";
    }

    public String deleteAuditorium(int auditoriumId) {
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

        if (CollectionUtils.isNotEmpty(auditorium.getScreenings())) {
            throw new NotAllowedException("There are/were screenings in this auditorium. " +
                    "Try deactivating it instead.");
        }

        auditoriumRepository.delete(auditorium);

        return "The auditorium and all its seats were removed from database.";
    }
}