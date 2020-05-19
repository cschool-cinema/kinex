package pl.termosteam.kinex.controller;

import static org.junit.Assert.assertEquals;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.ScreeningResponseDto;
import pl.termosteam.kinex.dto.SeatClientDto;
import pl.termosteam.kinex.service.ScreeningService;
import pl.termosteam.kinex.service.SeatService;
import pl.termosteam.kinex.service.UserService;

@ExtendWith(MockitoExtension.class)

public class ScreeningControllerTest {

	@Mock
	private ScreeningService screeningService;
	@Mock
	private SeatService seatService;
	@Mock
	private ModelMapper mm;
	@Mock
	private UserService userServiceMock;
	@InjectMocks
	private ScreeningController screeningController;

	public Screening screening;
	public Auditorium auditorium;
	public List<Ticket> tickets = new ArrayList<Ticket>();
	public User user1;
	public Seat seat1;
	public Seat seat2;
	public Screening screening1, screening2;
	public Movie movie1, movie2;
	List<Screening> screeningList = new ArrayList<>();

	@BeforeEach
	public void init() {
		user1 = new User("firstName", "lastName", "username", "email", "password", "salt", Role.USER.getRole(), "12323",
				true, true, LocalDateTime.now(), LocalDateTime.now());

		auditorium = new Auditorium(1, "name", Boolean.TRUE, new ArrayList<Seat>(), new ArrayList<Screening>());
		seat1 = new Seat(1,auditorium, new Short("1"),new Short("2"),Boolean.TRUE,new ArrayList<Ticket>());
		seat2 = new Seat(2,auditorium, new Short("2"),new Short("3"),Boolean.FALSE,new ArrayList<Ticket>());
		screening1= new Screening(1, movie1, auditorium, LocalDateTime.now(),tickets);
		screening2 = new Screening(2, movie2, auditorium, LocalDateTime.now(),tickets);
		screeningList.add(screening1);
		screeningList.add(screening2);
	}

	@Test
	public void findAvailableSeatsForScreening() {
		List<Seat> seats = Arrays.asList(seat1, seat2);
		SeatClientDto sdto1 = new SeatClientDto(seat1.getId(), seat1.getSeatRow().intValue(),
				seat1.getSeatNumber().intValue());
		SeatClientDto sdto2 = new SeatClientDto(seat2.getId(), seat2.getSeatRow().intValue(),
				seat2.getSeatNumber().intValue());
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(user1);
		when(seatService.findAvailableSeatsForScreening(1, Role.USER)).thenReturn(seats);
		when(mm.map(seats, SeatClientDto[].class)).thenReturn((new SeatClientDto[] { sdto1, sdto2 }));
		List<SeatClientDto> seatClientDtos = Arrays.asList(screeningController.findAvailableSeatsForScreening(1));
		assertEquals(2, seatClientDtos.size());
	}

	@Test
	public void findFutureScreeningsByMovieTitle() {
		String title = "aaa";
		ScreeningResponseDto sdto1 = new ScreeningResponseDto(1, LocalDateTime.now(),12, "aaa", new Short("1"), "a", "good", new Short("3"), 111, "aaa");
		ScreeningResponseDto sdto2 = new ScreeningResponseDto(3, LocalDateTime.now(),15, "bbb", new Short("1"), "c", "good", new Short("3"), 123, "ddd");

		when(screeningService.findFutureScreeningsByMovieTitle(title)).thenReturn(screeningList);
		when(mm.map(screeningList, ScreeningResponseDto[].class)).thenReturn((new ScreeningResponseDto[] { sdto1, sdto2 }));
		List<ScreeningResponseDto> screeningResponseDtos = Arrays.asList(screeningController.findFutureScreeningsByMovieTitle(title));
		assertEquals(2, screeningResponseDtos.size());
	}
}
