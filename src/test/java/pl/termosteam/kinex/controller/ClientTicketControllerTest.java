package pl.termosteam.kinex.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.domain.Movie;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.Screening;
import pl.termosteam.kinex.domain.Seat;
import pl.termosteam.kinex.domain.Ticket;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.*;
import pl.termosteam.kinex.service.TicketService;
import pl.termosteam.kinex.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class ClientTicketControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TicketService ticketServiceMock;
	@MockBean
	private UserService userServiceMock;

	private List<Ticket> tickets;
	private ObjectMapper mapper;

	@BeforeEach
	public void init() {
		tickets = new ArrayList<>();
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		User user1 = new User("firstName", "lastName", "username", "email", "password", "salt", Role.USER.getRole(), "12323",
				true, true, LocalDateTime.now(), LocalDateTime.now());
		User user2 = new User("firstName", "lastName", "username", "email", "password", "salt",
				Role.ADMINISTRATOR.getRole(), "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
		Movie movie = new Movie(1, "title", new Short("1"), "categorie", new Short("4"), "desc", new ArrayList<>());
		Auditorium auditorium = new Auditorium(1, "name", Boolean.TRUE, new ArrayList<>(), new ArrayList<>());
		Screening screening = new Screening(1, movie, auditorium, LocalDateTime.of(2020, 6, 1, 12, 10), tickets);
		Seat seat = new Seat(1, auditorium, new Short("3"), new Short("10"), true, tickets);
		Ticket ticket1 = new Ticket(1, user1, user2, screening, seat, true, null);
		Ticket ticket2 = new Ticket(2, user1, user2, screening, new Seat(), true, null);
		tickets.add(ticket1);
		tickets.add(ticket2);
	}

	@Test
	public void findUserTickets() throws Exception {
		Ticket t = tickets.get(0);
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(t.getUser());
		when(ticketServiceMock.findUserTickets(Mockito.anyInt())).thenReturn(tickets);
		MvcResult result = mockMvc.perform(get("/api/client/ticket").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		TicketResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), TicketResponseDto[].class);
		Assert.assertEquals(response.length, tickets.size());
	}

	@Test
	public void cancelReservationForScreening() throws Exception {
		String result = "Your reservation has been cancelled for this screening.";
		Ticket ticket = tickets.get(0);
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(ticket.getUser());
		when(ticketServiceMock.cancelReservationForScreening(any())).thenReturn(result);
		mockMvc.perform(put("/api/client/ticket/cancel-reservation/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(result));
	}

	@Test
	void makeReservation() throws Exception {
		Ticket ticket = tickets.get(0);
		int[] seats = {1,2};
		TicketRequestClientDto ticketRequestClientDto = new TicketRequestClientDto(ticket.getScreening().getId(), seats);
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(ticket.getUser());
		when(ticketServiceMock.makeReservation(Mockito.any(TicketRequestAdminDto.class), Mockito.any(User.class), Mockito.any(Role.class))).thenReturn(tickets);
		MvcResult result = mockMvc.perform(post("/api/client/ticket/make-reservation").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(ticketRequestClientDto))).andDo(print()).andExpect(status().isOk()).andReturn();
		TicketResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), TicketResponseDto[].class);
		Assert.assertEquals(response.length, tickets.size());
	}

	@Test
	void findUserTicketsForScreening() throws Exception {
		Ticket ticket = tickets.get(0);
		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(ticket.getUser());
		when(ticketServiceMock.findUserTicketsForScreening(Mockito.anyInt(), Mockito.anyInt())).thenReturn(tickets);
		MvcResult result = mockMvc.perform(get("/api/client/ticket/screening/1").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		TicketResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), TicketResponseDto[].class);
		Assert.assertEquals(response.length, tickets.size());
	}
}