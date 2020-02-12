package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.TicketRequestClientDto;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.TicketRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.SCREENING_NOT_FOUND;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatService seatService;

    private static final int CAN_BUY_MINUTES_AFTER_START = 30;

    public List<Ticket> findUserTickets(int userId) {
        return ticketRepository.findAllByUser_IdOrderByScreening(userId);
    }

    public List<Ticket> findUserTicketsForScreening(int userId, int screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        return ticketRepository.findAllByUser_IdAndScreening(userId, screening);
    }

    @Transactional
    public List<Ticket> makeReservation(TicketRequestClientDto ticketDto, User user) {
        int[] selectedSeats = ticketDto.getSeatIds();
        if (selectedSeats.length == 0) {
            throw new NotAllowedException("Please pick at least one seat.");
        }

        Set<Integer> selectedSeatsSet = Arrays.stream(
                selectedSeats).boxed().collect(Collectors.toSet());

        if (selectedSeatsSet.size() != selectedSeats.length) {
            throw new NotAllowedException("Make sure there are no duplicated seats chosen.");
        }

        Screening screening = screeningRepository.findById(ticketDto.getScreeningId())
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        if (LocalDateTime.now()
                .isAfter(screening.getScreeningStart().plusMinutes(CAN_BUY_MINUTES_AFTER_START))) {
            throw new NotAllowedException("Cannot make a reservation after 30 minutes into the screening start!");
        }

        List<Seat> availableSeatsList = seatService.findAvailableSeatsForScreening(screening.getId());
        Map<Integer, Seat> availableSeatsMap = availableSeatsList.stream().collect(
                Collectors.toMap(Seat::getId, seat -> seat));

        List<Ticket> tickets = new ArrayList<>();
        for (int seatId : selectedSeats) {
            if (availableSeatsMap.containsKey(seatId)) {
                Ticket ticket = Ticket.builder()
                        .user(user)
                        .seat(availableSeatsMap.get(seatId))
                        .screening(screening)
                        .active(true)
                        .build();

                tickets.add(ticket);
            } else {
                throw new NotAllowedException("One (or more) of the selected seats is not available");
            }
        }

        return ticketRepository.saveAll(tickets);
    }
}
