package pl.termosteam.kinex.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.termosteam.kinex.domain.*;
import pl.termosteam.kinex.dto.*;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminTicketControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private TicketService ticketService;
    @Mock
    private ModelMapper mm;
    @InjectMocks
    private AdminTicketController adminTicketController;

    private Ticket ticket1, ticket2, ticket3;
    private TicketResponseDto[] allTicketResponseDtoList = new TicketResponseDto[3];
    private TicketResponseDto ticketResponseDto;
    private TicketCancelRequestDto ticketCancelRequestDto;
    private TicketRequestAdminDto ticketRequestAdminDto;
    private TicketResponseDto ticketResponseDto1, ticketResponseDto2, ticketResponseDto3;
    List<Ticket> allTicket = new ArrayList<Ticket>();
    private User reservedByUser;
    private Role reservedByRole;
    private User user1,user2;

    @BeforeEach
    public void init(){
        user1 = new User("first", "first_last", "user1", "@email1", "password", "salt", Role.USER.getRole(),
                "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
        user2 = new User("second", "second_last", "username2", "@email2", "password", "salt",
                Role.ADMINISTRATOR.getRole(), "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
        ticket1 = new Ticket(11, user1, user2, new Screening(), new Seat(), true,LocalDateTime.of(2011, 4, 5, 12, 34));
        ticket1 = new Ticket(11, user1, user2, new Screening(), new Seat(), false,LocalDateTime.of(2012, 4, 5, 12, 34));
        ticket1 = new Ticket(11, user1, user2, new Screening(), new Seat(), true,LocalDateTime.of(2013, 4, 5, 12, 34));
        allTicket.add(ticket1);
        allTicket.add(ticket2);
        allTicket.add(ticket3);
    }

    @Test
    void cancelReservationForScreening() {
        String result = "Your reservation has been cancelled for this screening.";
        when(ticketService.cancelReservationForScreening(ticketCancelRequestDto)).thenReturn(result);
        String cancelReservationResult = adminTicketController.cancelReservationForScreening(ticketCancelRequestDto);
        assertEquals(result, cancelReservationResult);
    }
    @Test
    void makeReservation(){
        when(userService.getUserNotNullIfAuthenticated()).thenReturn(user1);
        when(ticketService.makeReservation(ticketRequestAdminDto, user2, Role.MANAGER)).thenReturn(allTicket);
        when(mm.map(allTicket, TicketResponseDto[].class)).thenReturn((new TicketResponseDto[] { ticketResponseDto1, ticketResponseDto2, ticketResponseDto3 }));
        List<TicketResponseDto> makeReservationResult = Arrays.asList(adminTicketController.makeReservation(ticketRequestAdminDto));
        assertEquals(3, makeReservationResult.size());
    }
}