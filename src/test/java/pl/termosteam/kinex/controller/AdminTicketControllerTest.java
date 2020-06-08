package pl.termosteam.kinex.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.termosteam.kinex.domain.*;
import pl.termosteam.kinex.dto.*;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"MANAGER"})
class AdminTicketControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private TicketService ticketService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private List<Ticket> allTicket;

    @BeforeEach
    public void init(){
        allTicket = new ArrayList<>();
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        User user1 = new User("first", "first_last", "user1", "@email1", "password", "salt",
                Role.USER.getRole(),"12323", true, true, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User("second", "second_last", "username2", "@email2", "password",
                "salt", Role.ADMINISTRATOR.getRole(), "12323", true, true,
                LocalDateTime.now(), LocalDateTime.now());
        Ticket ticket1 = new Ticket(11, user1, user2, new Screening(), new Seat(), true,
                LocalDateTime.of(2011, 4, 5, 12, 34));
        Ticket ticket2 = new Ticket(11, user1, user2, new Screening(), new Seat(), false,
                LocalDateTime.of(2012, 4, 5, 12, 34));
        Ticket ticket3 = new Ticket(11, user1, user2, new Screening(), new Seat(), true,
                LocalDateTime.of(2013, 4, 5, 12, 34));

        allTicket.add(ticket1);
        allTicket.add(ticket2);
        allTicket.add(ticket3);
    }

    @Test
    void cancelReservationForScreening() throws Exception {
        String result = "Your reservation has been cancelled for this screening.";
        Ticket ticket = allTicket.get(0);
        when(ticketService.cancelReservationForScreening(Mockito.any(TicketCancelRequestDto.class))).thenReturn(result);
        TicketCancelRequestDto ticketCancelRequestDto = new TicketCancelRequestDto(ticket.getUser().getId(), 1);
        mockMvc.perform(put("/api/admin/ticket/cancel-reservation").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(ticketCancelRequestDto)))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    void makeReservation() throws Exception {
        Ticket ticket = allTicket.get(0);
        int[] seats = {1,2};
        TicketRequestAdminDto ticketRequestAdminDto = new TicketRequestAdminDto(ticket.getUser().getId(), 1, seats);
        when(userService.getUserNotNullIfAuthenticated()).thenReturn(ticket.getUser());
        when(ticketService.makeReservation(Mockito.any(TicketRequestAdminDto.class), Mockito.any(User.class), Mockito.any(Role.class))).thenReturn(allTicket);
        MvcResult result = mockMvc.perform(post("/api/admin/ticket/make-reservation").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(ticketRequestAdminDto))).andDo(print()).andExpect(status().isOk()).andReturn();
        TicketResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), TicketResponseDto[].class);
        Assert.assertEquals(response.length, allTicket.size());
    }
}