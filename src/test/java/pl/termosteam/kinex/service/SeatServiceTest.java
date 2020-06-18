package pl.termosteam.kinex.service;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.termosteam.kinex.domain.*;
import pl.termosteam.kinex.exception.NotFoundException;
import pl.termosteam.kinex.repository.AuditoriumRepository;
import pl.termosteam.kinex.repository.ScreeningRepository;
import pl.termosteam.kinex.repository.SeatRepository;
import pl.termosteam.kinex.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {

    @Mock
    private ScreeningRepository screeningRepositoryMock;
    @Mock
    private TicketRepository ticketRepositoryMock;
    @Mock
    private SeatRepository seatRepositoryMock;
    @Mock
    private AuditoriumRepository auditoriumRepositoryMock;

    @InjectMocks
    private SeatService seatService;

    private List<Integer> integers;
    private Movie movie;
    private Auditorium auditorium;
    private Seat seat1;
    private Seat seat2;
    private List<Seat> seats;
    private Screening screening;


    @BeforeEach
    public void init() {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);
        integers=new ArrayList<>();
        integers.add(new Integer("1"));
        movie = new Movie(1, "title", new Short("1"), "category", new Short("4"), "desc", new ArrayList<>());
        auditorium=new Auditorium(1, "name", Boolean.TRUE, new ArrayList<>(), new ArrayList<>());
        screening = new Screening(1, movie, auditorium, LocalDateTime.now(), tickets);
        seat1 = new Seat(1,auditorium, new Short("1"),new Short("2"),Boolean.TRUE,new ArrayList<>());
        seat2 = new Seat(2,auditorium, new Short("2"),new Short("3"),Boolean.FALSE,new ArrayList<>());
        seats=new ArrayList<>();
        seats.add(seat1);
        seats.add(seat2);
    }

    @Rule
    ExpectedException exception = ExpectedException.none();

    @Test
    public void findAvailableSeatsForScreeningException() {
        exception.expect(NotFoundException.class);
        exception.expectMessage("SCREENING_NOT_FOUND");
    }

    @Test
    public void findAvailableSeatsForScreeningWithTwoParam() {
        given(screeningRepositoryMock.findById(1)).willReturn(Optional.of(screening));
        given(ticketRepositoryMock.findAllReservedSeatsForScreening(1)).willReturn(integers);
        given(seatRepositoryMock.findByActiveAndAuditoriumIdAndExcludingSeatIds(1, integers)).willReturn(seats);
        List<Seat> seatList= seatService.findAvailableSeatsForScreening(1,Role.OWNER);
        assertEquals(seats, seatList);
    }

    @Test
    public void findAvailableSeatsForScreeningWithOneParam() {
        given(screeningRepositoryMock.findById(1)).willReturn(Optional.of(screening));
        given(ticketRepositoryMock.findAllReservedSeatsForScreening(1)).willReturn(integers);
        given(seatRepositoryMock.findByActiveAndAuditoriumIdAndExcludingSeatIds(1, integers)).willReturn(seats);
        List<Seat> seatList= seatService.findAvailableSeatsForScreening(1);
        assertEquals(seats, seatList);
    }

    @Test
    public void addSeat() {
        given(auditoriumRepositoryMock.findById(1)).willReturn(Optional.of(auditorium));
        given(seatRepositoryMock.save(seat1)).willReturn(seat1);
        Seat seat=seatService.addSeat(seat1,1);
        assertEquals(seat1, seat);
    }

    @Test
    public void deactivateSeat() {
        given(seatRepositoryMock.findById(1)).willReturn(Optional.of(seat1));
        String res=seatService.deactivateSeat(1);
        assertEquals(res, "The seat has been deactivated.");
    }

    @Test
    public void reactivateSeat() {
        given(seatRepositoryMock.findById(2)).willReturn(Optional.of(seat2));
        String res=seatService.reactivateSeat(2);
        assertEquals(res, "The seat has been reactivated.");
    }

    @Test
    public void deleteSeat() {
        given(seatRepositoryMock.findById(1)).willReturn(Optional.of(seat1));
        String res=seatService.deleteSeat(1);
        assertEquals(res, "The seat has been deleted.");
    }
}




