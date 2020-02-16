package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.AuditoriumRepository;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.SeatRepository;
import pl.termosteam.kinex.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.List;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.*;

@Service
@AllArgsConstructor
public class SeatService {

    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final AuditoriumRepository auditoriumRepository;

    public List<Seat> findAvailableSeatsForScreening(int screeningId, Role requesterRole) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        if (screening.getScreeningStart().isBefore(LocalDateTime.now()) &&
                requesterRole.getHierarchy() < 2) {
            throw new AccessDeniedException(FORBIDDEN);
        }

        List<Integer> reservedSeats = ticketRepository.findAllReservedSeatsForScreening(screeningId);

        int auditoriumId = screening.getAuditorium().getId();

        if (CollectionUtils.isEmpty(reservedSeats)) {
            return seatRepository
                    .findByActiveTrueAndAuditoriumIdOrderBySeatRowAscSeatNumberAsc(auditoriumId);
        }

        return seatRepository
                .findByActiveAndAuditoriumIdAndExcludingSeatIds(auditoriumId, reservedSeats);
    }

    public Seat addSeat(Seat seat, int auditoriumId) {
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
                .orElseThrow(() -> new NotFoundException(AUDITORIUM_NOT_FOUND));

        if (seatRepository.existsBySeatNumberAndSeatRowAndAuditorium_Id(
                seat.getSeatNumber(), seat.getSeatRow(), auditoriumId)) {
            throw new NotAllowedException("Seat (row, number) already exists in this auditorium");
        }

        seat.setAuditorium(auditorium);

        return seatRepository.save(seat);
    }

    public String deactivateSeat(int seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException(SEAT_NOT_FOUND));

        if (!seat.getActive()) {
            throw new NotAllowedException("The seat is already inactive.");
        }

        if (seatRepository.existsActiveTicketsStartingFrom(seatId, LocalDateTime.now())) {
            throw new NotAllowedException("Cannot deactivate! Active, future tickets exist for this seat.");
        }

        List<Seat> seats = seat.getAuditorium().getSeats();
        int activeSeats = 0;
        for (Seat s : seats) {
            if (s.getActive()) activeSeats++;
        }

        if (activeSeats == 1) {
            throw new NotAllowedException("This is the last active seat. " +
                    "Please deactivate whole auditorium instead.");
        }

        seat.setActive(false);

        seatRepository.save(seat);

        return "The seat has been deactivated.";
    }

    public String reactivateSeat(int seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException(SEAT_NOT_FOUND));

        if (seat.getActive()) {
            throw new NotAllowedException("Seat is already active.");
        }

        seat.setActive(true);

        seatRepository.save(seat);

        return "The seat has been reactivated.";
    }

    public String deleteSeat(int seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NotFoundException(SEAT_NOT_FOUND));

        if (CollectionUtils.isNotEmpty(seat.getTickets())) {
            throw new NotAllowedException("Tickets have been sold for this seat. " +
                    "Try deactivating the seat instead.");
        }

        seatRepository.delete(seat);

        return "The seat has been deleted.";
    }
}
