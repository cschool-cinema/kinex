package pl.termosteam.kinex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.dto.ScreeningRequestDto;
import pl.termosteam.kinex.exception.NotAllowedException;
import pl.termosteam.kinex.repository.AuditoriumRepository;
import pl.termosteam.kinex.repository.MovieRepository;
import pl.termosteam.kinex.repository.ScreeningRepository;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTest {

	@Mock
	private ScreeningRepository screeningRepositoryMock;
	@Mock
	private MovieRepository movieRepositoryMock;
	@Mock
	private AuditoriumRepository auditoriumRepositoryMock;

	@InjectMocks
	private ScreeningService screeningService;

	public Screening screening;
	public Movie movie;
	public Auditorium auditorium;
	public Auditorium inactiveAuditorium;
	public Screening screeningWithNullAuditorium;

	@BeforeEach
	public void init() {
		Ticket ticket1 = new Ticket();
		Ticket ticket2 = new Ticket();
		List<Ticket> tickets = new ArrayList<Ticket>();
		tickets.add(ticket1);
		tickets.add(ticket2);
		movie = new Movie(1, "title", new Short("1"), "categorie", new Short("4"), "desc", new ArrayList<Screening>());
		auditorium = new Auditorium(1, "name", Boolean.TRUE, new ArrayList<Seat>(), new ArrayList<Screening>());
		inactiveAuditorium = new Auditorium(1, "name", Boolean.FALSE, new ArrayList<Seat>(), new ArrayList<Screening>());
		screening = new Screening(1, movie, auditorium, LocalDateTime.now(), tickets);
		screeningWithNullAuditorium = new Screening(1, movie, null, LocalDateTime.now(), tickets);
	}

	@Test
	public void findScreeningsStartingFrom() {
		Screening screening1 = new Screening();
		Screening screening2 = new Screening();
		Screening screening3 = new Screening();
		List<Screening> screenings = new ArrayList<Screening>();
		screenings.add(screening1);
		screenings.add(screening2);
		screenings.add(screening3);

		given(screeningRepositoryMock.findAllByScreeningStartGreaterThanEqualOrderByScreeningStart(Mockito.any()))
				.willReturn(screenings);

		List<Screening> screeningList = screeningService.findScreeningsStartingFrom(LocalDateTime.now());
		assertEquals(screenings.size(), screeningList.size());
	}

	@Test
	public void findAllScreenings() {
		Screening screening1 = new Screening();
		Screening screening2 = new Screening();
		List<Screening> screenings = new ArrayList<Screening>();
		screenings.add(screening1);
		screenings.add(screening2);

		given(screeningRepositoryMock.findAllByScreeningStartGreaterThanEqualOrderByScreeningStart(Mockito.any()))
				.willReturn(screenings);

		List<Screening> screeningList = screeningService.findScreeningsStartingFrom(LocalDateTime.now());
		assertEquals(screenings.size(), screeningList.size());
	}

	@Test
	public void findScreeningById() {
		given(screeningRepositoryMock.findById(1)).willReturn(Optional.of(screening));
		Screening screening = screeningService.findScreeningById(1);
		assertEquals(1, screening.getId());
		assertEquals(2, screening.getTickets().size());
	}

	@Test
	public void createScreeningNormalCase() {
		when(auditoriumRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(auditorium));
		when(movieRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(movie));
		when(screeningRepositoryMock.save(Mockito.any())).thenReturn(screening);
		Screening savedScreening = screeningService.createScreening(new ScreeningRequestDto(LocalDateTime.now(), 1, 1));
		assertEquals(1, savedScreening.getId());
	}

	@Test
	public void createScreeningWithInactiveAuditorium() {
		when(auditoriumRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(inactiveAuditorium));
		when(movieRepositoryMock.findById(Mockito.any())).thenReturn(Optional.of(movie));
		Assertions.assertThrows(NotAllowedException.class,
				() -> screeningService.createScreening(new ScreeningRequestDto(LocalDateTime.now(), 1, 1)));
	}
}
