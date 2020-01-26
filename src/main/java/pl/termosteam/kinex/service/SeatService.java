package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.SeatRepository;
import pl.termosteam.kinex.repository.TicketRepository;

import javax.transaction.Transactional;
import java.util.List;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.SCREENING_NOT_FOUND;

@Service
@AllArgsConstructor
public class SeatService {

    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public List<Seat> findAvailableSeatsForScreening(int screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        List<Integer> reservedSeats = ticketRepository.findAllReservedSeatsForScreening(screeningId);

        int auditoriumId = screening.getAuditorium().getId();

        if (reservedSeats.size() == 0) {
            return seatRepository
                    .findByActiveTrueAndAuditoriumIdOrderBySeatRowAscSeatNumberAsc(auditoriumId);
        }

        return seatRepository
                .findByActiveAndAuditoriumIdAndExcludingSeatIds(auditoriumId, reservedSeats);
    }
}
