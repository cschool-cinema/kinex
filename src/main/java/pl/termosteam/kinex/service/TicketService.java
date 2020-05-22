package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.termosteam.kinex.domain.*;
import pl.termosteam.kinex.dto.TicketCancelRequestDto;
import pl.termosteam.kinex.dto.TicketRequestAdminDto;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.TicketRepository;
import pl.termosteam.kinex.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.SCREENING_NOT_FOUND;
import static pl.termosteam.kinex.exception.StandardExceptionResponseRepository.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Transactional
public class TicketService {

    @Value("${business.can-reserve-min-after-screening-start}")
    private static int canReserveMinutesAfterStart;

    @Value("${business.can-reserve-max-seats}")
    private static int canReserveMaxSeats;

    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;
    private final UserRepository userRepository;
    private final SeatService seatService;

    public List<Ticket> findUserTickets(int userId) {
        return ticketRepository.findAllByUser_IdOrderByScreening(userId);
    }

    public List<Ticket> findUserTicketsForScreening(int userId, int screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        return ticketRepository.findAllByUser_IdAndScreening(userId, screening);
    }

    public List<Ticket> makeReservation(TicketRequestAdminDto ticketInfo, User reservedBy, Role reservedByRole) {
        int[] selectedSeatIds = ticketInfo.getSeatIds();

        if (selectedSeatIds.length == 0) {
            throw new NotAllowedException("Please pick at least one seat.");
        } else if (selectedSeatIds.length > canReserveMaxSeats && reservedByRole.getHierarchy() < 2) {
            throw new NotAllowedException("Please pick not more than " + canReserveMaxSeats + " seats.");
        }

        Set<Integer> selectedSeatsSet = Arrays.stream(
                selectedSeatIds).boxed().collect(Collectors.toSet());
        if (selectedSeatsSet.size() != selectedSeatIds.length) {
            throw new NotAllowedException("Make sure there are no duplicated seats chosen.");
        }

        User user = userRepository.findById(ticketInfo.getUserId())
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        Screening screening = screeningRepository.findById(ticketInfo.getScreeningId())
                .orElseThrow(() -> new NotFoundException(SCREENING_NOT_FOUND));

        if (LocalDateTime.now()
                .isAfter(screening.getScreeningStart().plusMinutes(canReserveMinutesAfterStart))) {
            throw new NotAllowedException("Cannot make a reservation after " +
                    canReserveMinutesAfterStart + " minutes into the screening start!");
        }

        List<Seat> availableSeatsList = seatService.findAvailableSeatsForScreening(screening.getId());
        Map<Integer, Seat> availableSeatsMap = availableSeatsList.stream().collect(
                Collectors.toMap(Seat::getId, Function.identity()));

        List<Seat> selectedSeatsList = new ArrayList<>();

        List<Ticket> tickets = new ArrayList<>();
        for (int seatId : selectedSeatIds) {
            if (availableSeatsMap.containsKey(seatId)) {
                Seat seat = availableSeatsMap.get(seatId);

                Ticket ticket = Ticket.builder()
                        .user(user)
                        .reservedByUser(reservedBy)
                        .seat(seat)
                        .screening(screening)
                        .active(true)
                        .build();

                selectedSeatsList.add(seat);
                tickets.add(ticket);
            } else {
                throw new NotAllowedException("One (or more) of the selected seats is not available.");
            }
        }

        if (reservedByRole.getHierarchy() < 2 &&
                existsSingleGapBetweenSeats(availableSeatsList, selectedSeatsList)) {
            throw new NotAllowedException("Single seat gaps are not allowed.");
        }

        return ticketRepository.saveAll(tickets);
    }

    public String cancelReservationForScreening(TicketCancelRequestDto ticketInfo) {
        List<Ticket> reservedTickets = ticketRepository
                .findAllByUser_IdAndScreening_IdAndActiveTrue(ticketInfo.getUserId(), ticketInfo.getScreeningId());

        if (CollectionUtils.isEmpty(reservedTickets)) {
            throw new NotFoundException("Could not find any active reservations for this user/screening.");
        }

        if (LocalDateTime.now().isAfter(reservedTickets.get(0).getScreening().getScreeningStart())) {
            throw new NotAllowedException("Cannot cancel reservation. The screening start time already passed!");
        }

        for (Ticket ticket : reservedTickets) {
            ticket.setActive(false);
        }

        ticketRepository.saveAll(reservedTickets);

        return "Your reservation has been cancelled for this screening.";
    }

    private boolean existsSingleGapBetweenSeats(List<Seat> availableSeats,
                                                List<Seat> selectedSeats) {
        availableSeats.removeAll(selectedSeats);

        Map<Pair<Short, Short>, Seat> availableSeatsMap = new HashMap<>();

        for (Seat seat : availableSeats) {
            availableSeatsMap.put(new ImmutablePair<>(seat.getSeatRow(), seat.getSeatNumber()), seat);
        }

        for (Seat seat : selectedSeats) {
            short seatRow = seat.getSeatRow();
            short seatNumber = seat.getSeatNumber();

            Pair<Short, Short> oneToRight = new ImmutablePair<>(seatRow, (short) (seatNumber - 1));
            Pair<Short, Short> twoToRight = new ImmutablePair<>(seatRow, (short) (seatNumber - 2));
            if (availableSeatsMap.get(oneToRight) != null && availableSeatsMap.get(twoToRight) == null) {
                return true;
            }

            Pair<Short, Short> oneToLeft = new ImmutablePair<>(seatRow, (short) (seatNumber + 1));
            Pair<Short, Short> twoToLeft = new ImmutablePair<>(seatRow, (short) (seatNumber + 2));
            if (availableSeatsMap.get(oneToLeft) != null && availableSeatsMap.get(twoToLeft) == null) {
                return true;
            }
        }

        return false;
    }
}
