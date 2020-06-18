package pl.termosteam.kinex.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import pl.termosteam.kinex.dto.ScreeningResponseDto;
import pl.termosteam.kinex.dto.SeatClientDto;
import pl.termosteam.kinex.service.ScreeningService;
import pl.termosteam.kinex.service.SeatService;
import pl.termosteam.kinex.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"GUEST"})
public class ScreeningControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ScreeningService screeningService;
	@MockBean
	private SeatService seatService;
	@MockBean
	private UserService userServiceMock;
	private ObjectMapper mapper;

	public Auditorium auditorium;
	public List<Ticket> tickets = new ArrayList<>();
	public Screening screening1, screening2;
	public Movie movie1, movie2;
	List<Screening> screeningList = new ArrayList<>();

	@BeforeEach
	public void init() {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		auditorium = new Auditorium(1, "name", Boolean.TRUE, new ArrayList<>(), new ArrayList<>());
		screening1= new Screening(1, movie1, auditorium, LocalDateTime.now(),tickets);
		screening2 = new Screening(2, movie2, auditorium, LocalDateTime.now(),tickets);
		screeningList.add(screening1);
		screeningList.add(screening2);
	}

	@Test
	public void findAvailableSeatsForScreening() throws Exception {
		User user1 = new User("firstName", "lastName", "username", "email", "password", "salt", Role.USER.getRole(), "12323",
				true, true, LocalDateTime.now(), LocalDateTime.now());
		Seat seat1 = new Seat(1,auditorium, new Short("1"),new Short("2"),Boolean.TRUE,new ArrayList<>());
		Seat seat2 = new Seat(2,auditorium, new Short("2"),new Short("3"),Boolean.FALSE,new ArrayList<>());

		List<Seat> seats = Arrays.asList(seat1, seat2);

		when(userServiceMock.getUserNotNullIfAuthenticated()).thenReturn(user1);
		when(seatService.findAvailableSeatsForScreening(1, Role.USER)).thenReturn(seats);

		MvcResult result = mockMvc.perform(get("/api/screening/1/available-seats").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		SeatClientDto[] response = mapper.readValue(result.getResponse().getContentAsString(), SeatClientDto[].class);
		Assert.assertEquals(response.length, seats.size());
	}

	@Test
	public void findFutureScreeningsByMovieTitle() throws Exception {
		when(screeningService.findFutureScreeningsByMovieTitle(Mockito.anyString())).thenReturn(screeningList);
		MvcResult result = mockMvc.perform(get("/api/screening/aaa").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		ScreeningResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), ScreeningResponseDto[].class);
		Assert.assertEquals(response.length, screeningList.size());
	}

	@Test
	public void findFutureScreenings() throws Exception {
		when(screeningService.findScreeningsStartingFrom(Mockito.any(LocalDateTime.class))).thenReturn(screeningList);
		MvcResult result = mockMvc.perform(get("/api/screening").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		ScreeningResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), ScreeningResponseDto[].class);
		Assert.assertEquals(response.length, screeningList.size());
	}
}
