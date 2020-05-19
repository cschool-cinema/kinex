package pl.termosteam.kinex.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.TicketResponseDto;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;

@ExtendWith(MockitoExtension.class)

public class ClientTicketControllerTest {

	@Mock
	private TicketService ticketServiceMock;
	@Mock
	private ModelMapper mm;
	@Mock
	private UserService userServiceMock;
	@InjectMocks
	private ClientTicketController clientTicketController;

	public Screening screening;
	public Movie movie;
	public Auditorium auditorium;
	public Ticket ticket1;
	public Ticket ticket2;
	public List<Ticket> tickets = new ArrayList<Ticket>();
	public User user1;

	@BeforeEach
	public void init() {
		user1 = new User("firstName", "lastName", "username", "email", "password", "salt", Role.USER.getRole(), "12323",
				true, true, LocalDateTime.now(), LocalDateTime.now());
		User user2 = new User("firstName", "lastName", "username", "email", "password", "salt",
				Role.ADMINISTRATOR.getRole(), "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
		movie = new Movie(1, "title", new Short("1"), "categorie", new Short("4"), "desc", new ArrayList<Screening>());
		auditorium = new Auditorium(1, "name", Boolean.TRUE, new ArrayList<Seat>(), new ArrayList<Screening>());
		screening = new Screening(1, movie, auditorium, LocalDateTime.of(2020, 6, 1, 12, 00), tickets);
		Seat seat = new Seat(1, auditorium, new Short("3"), new Short("10"), true, tickets);
		ticket1 = new Ticket(1, user1, user2, screening, seat, true, null);
		ticket2 = new Ticket(2, user1, user2, screening, new Seat(), true, null);
		tickets.add(ticket1);
		tickets.add(ticket2);
	}

	@Test
	public void findUserTickets() {
		Ticket t = tickets.get(0);
		TicketResponseDto dto = new TicketResponseDto(t.getId(), t.getScreening().getMovie().getTitle(),
				t.getScreening().getScreeningStart(), t.getScreening().getAuditorium().getName(),
				t.getSeat().getSeatRow(), t.getSeat().getSeatNumber(), t.getActive());
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(user1);
		when(ticketServiceMock.findUserTickets(Mockito.anyInt())).thenReturn(tickets);
		when(mm.map(tickets, TicketResponseDto[].class)).thenReturn((new TicketResponseDto[] { dto }));
		List<TicketResponseDto> ticketResponseDtos = Arrays.asList(clientTicketController.findUserTickets());
		assertEquals(dto, ticketResponseDtos.get(0));
	}

	@Test
	public void cancelReservationForScreening(){
		String result = "Your reservation has been cancelled for this screening.";
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(user1);
		when(ticketServiceMock.cancelReservationForScreening(any())).thenReturn(result);
		String cancelReservationResult = clientTicketController.cancelReservationForScreening(12);
		assertEquals(result, cancelReservationResult);
	}
}
