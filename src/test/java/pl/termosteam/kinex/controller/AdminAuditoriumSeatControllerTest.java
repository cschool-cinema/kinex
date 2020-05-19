package pl.termosteam.kinex.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.dto.SeatAdminDto;
import pl.termosteam.kinex.service.SeatService;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import pl.termosteam.kinex.domain.Seat;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdminAuditoriumSeatControllerTest {

    @Mock
    private SeatService seatService;
    @Mock
    private ModelMapper mm;
    @InjectMocks
    private AdminAuditoriumSeatController adminAuditoriumSeatController;
    private Seat seat1, seat2, seat3;
    private SeatAdminDto[] allSeatDtoList = new SeatAdminDto[3];
    private SeatAdminDto seatAdminDto1, seatAdminDto2, seatAdminDto3;
    List<Seat> allSeat = new ArrayList<Seat>();

    @BeforeEach
    public void init(){
        seat1 = new Seat(11, new Auditorium(), new Short("3"), new Short("10"), true, new ArrayList<Ticket>());
        seat2 = new Seat(12, new Auditorium(), new Short("3"), new Short("10"), true, new ArrayList<Ticket>());
        seat3 = new Seat(13, new Auditorium(), new Short("3"), new Short("10"), true, new ArrayList<Ticket>());
        allSeat.add(seat1);
        allSeat.add(seat2);
        allSeat.add(seat3);
    }

    @Test
    void addSeat() {
        when(seatService.addSeat(seat1, 11)).thenReturn(seat1);
        when(mm.map(seatAdminDto1, Seat.class)).thenReturn(seat1);
        when(mm.map(seat1, SeatAdminDto.class)).thenReturn(seatAdminDto1);
        SeatAdminDto addSeatDto = adminAuditoriumSeatController.addSeat(seatAdminDto1, 11);
        assertEquals(seatAdminDto1, addSeatDto);
    }

    @Test
    void deactivateSeat() {
        String result = "The seat has been deactivated.";
        when(seatService.deactivateSeat(11)).thenReturn(result);
        String deactivateSeatResult = adminAuditoriumSeatController.deactivateSeat(11);
        assertEquals(result, deactivateSeatResult);
    }

    @Test
    void reactivateSeat() {
        String result = "The seat has been reactivated.";
        when(seatService.reactivateSeat(13)).thenReturn(result);
        String reactiveSeatResult = adminAuditoriumSeatController.reactivateSeat(13);
        assertEquals(result, reactiveSeatResult);
    }

    @Test
    void deleteSeat() {
        String result = "The seat has been deleted.";
        when(seatService.deleteSeat(12)).thenReturn(result);
        String deleteSeatResult = adminAuditoriumSeatController.deleteSeat(12);
        assertEquals(result, deleteSeatResult);
    }
}