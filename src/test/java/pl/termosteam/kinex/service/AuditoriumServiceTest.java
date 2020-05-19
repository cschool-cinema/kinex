package pl.termosteam.kinex.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.repository.AuditoriumRepository;

@ExtendWith(MockitoExtension.class)
public class AuditoriumServiceTest {

	@Mock
	private AuditoriumRepository auditoriumRepositoryMock;

	@InjectMocks
	private AuditoriumService auditoriumService;

	private Auditorium auditorium1;
	private Auditorium auditorium2;
	private Seat seat1;
	private Seat seat2;
	private List<Seat> seatList;

	@BeforeEach
	public void init() {
		seat1 = new Seat(1,auditorium1, new Short("1"),new Short("2"),Boolean.TRUE,new ArrayList<Ticket>());
		seat2 = new Seat(2,auditorium2, new Short("2"),new Short("3"),Boolean.FALSE,new ArrayList<Ticket>());
		seatList=new ArrayList<Seat>();
		seatList.add(seat1);
		seatList.add(seat2);
		auditorium1 = new Auditorium(1, "name", Boolean.TRUE, seatList, new ArrayList<Screening>());
		auditorium2 = new Auditorium(2, "name", Boolean.FALSE, seatList, new ArrayList<Screening>());
	}

	@Test
	public void findAllAuditoriums() {
		List<Auditorium> auditoriums = new ArrayList<Auditorium>();
		auditoriums.add(auditorium1);
		auditoriums.add(auditorium2);
		given(auditoriumRepositoryMock.findAll(Sort.by(Sort.Direction.ASC, "name"))).willReturn(auditoriums);
		List<Auditorium> auList = auditoriumService.findAllAuditoriums();
		assertEquals(2, auList.size());
	}


	@Test
	public void findAuditoriumById() {
		given(auditoriumRepositoryMock.findById(1)).willReturn(Optional.of(auditorium1));
		Auditorium res = auditoriumService.findAuditoriumById(1);
		assertEquals(auditorium1, res);
	}

	@Test
	public void createAuditorium() {
		given(auditoriumRepositoryMock.save(auditorium1)).willReturn(auditorium1);
		Auditorium res = auditoriumService.createAuditorium(auditorium1);
		assertEquals(auditorium1, res);
	}


	@Test
	public void deactivateAuditorium() {
		given(auditoriumRepositoryMock.findById(1)).willReturn(Optional.of(auditorium1));
		given(auditoriumRepositoryMock.save(auditorium1)).willReturn(auditorium1);
		String s = auditoriumService.deactivateAuditorium(1);
		assertEquals(s, "The auditorium and all its seats have been deactivated.");
	}

	@Test
	public void reactivateAuditorium() {
		given(auditoriumRepositoryMock.findById(2)).willReturn(Optional.of(auditorium2));
		given(auditoriumRepositoryMock.save(auditorium2)).willReturn(auditorium2);
		String s = auditoriumService.reactivateAuditorium(2);
		assertEquals(s, "The auditorium and all its seats have been reactivated.");
	}

	@Test
	public void deleteAuditorium() {
		given(auditoriumRepositoryMock.findById(1)).willReturn(Optional.of(auditorium1));
		String s = auditoriumService.deleteAuditorium(1);
		assertEquals(s, "The auditorium and all its seats were removed from database.");
	}

}
