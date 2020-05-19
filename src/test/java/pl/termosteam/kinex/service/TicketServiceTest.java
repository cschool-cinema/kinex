package pl.termosteam.kinex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.TicketCancelRequestDto;
import pl.termosteam.kinex.dto.TicketRequestAdminDto;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.TicketRepository;
import pl.termosteam.kinex.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

	@Mock
	private TicketRepository ticketRepository;
	@Mock
	private ScreeningRepository screeningRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private SeatService seatService;

	@InjectMocks
	private TicketService ticketService;

	private static final int CAN_RESERVE_MAX_SEATS = 100;

	public Screening screening;
	public Movie movie;
	public Auditorium auditorium;
	public Auditorium inactiveAuditorium;

	public Ticket ticket1;
	public Ticket ticket2;
	public List<Ticket> tickets = new ArrayList<Ticket>();

	@BeforeEach
	public void init() {
		User user1 = new User("firstName", "lastName", "username", "email", "password", "salt", Role.USER.getRole(),
				"12323", true, true, LocalDateTime.now(), LocalDateTime.now());
		User user2 = new User("firstName", "lastName", "username", "email", "password", "salt",
				Role.ADMINISTRATOR.getRole(), "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
		movie = new Movie(1, "title", new Short("1"), "category", new Short("4"), "desc", new ArrayList<Screening>());
		auditorium = new Auditorium(1, "name", Boolean.TRUE, new ArrayList<Seat>(), new ArrayList<Screening>());
		screening = new Screening(1, movie, auditorium, LocalDateTime.of(2020, 6, 1, 12, 00), tickets);
		ticket1 = new Ticket(1, user1, user2, screening, new Seat(), true, null);
		ticket2 = new Ticket(2, user1, user2, screening, new Seat(), true, null);
		tickets.add(ticket1);
		tickets.add(ticket2);
	}

	@Test
	public void makeReservationWithEmptySeats() {
		Assertions.assertThrows(NotAllowedException.class, () -> ticketService
				.makeReservation(new TicketRequestAdminDto(1, 1, new int[] {}), new User(), Role.USER));
	}

	@Test
	public void makeReservationWithEmptyNumberSeatsExceedLimit() {
		Assertions.assertThrows(NotAllowedException.class,
				() -> ticketService.makeReservation(
						new TicketRequestAdminDto(1, 1, IntStream.range(0, CAN_RESERVE_MAX_SEATS + 1).toArray()),
						new User(), Role.USER));
	}

	@Test
	public void cancelReservationForScreening() {
		TicketCancelRequestDto ticketInfo = new TicketCancelRequestDto(1, 1);
		when(ticketRepository.findAllByUser_IdAndScreening_IdAndActiveTrue(ticketInfo.getUserId(),
				ticketInfo.getScreeningId())).thenReturn(tickets);
		String s = ticketService.cancelReservationForScreening(ticketInfo);
		assertEquals(false, tickets.get(0).getActive());
	}
}
