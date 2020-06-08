package pl.termosteam.kinex.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.termosteam.kinex.domain.Auditorium;
import pl.termosteam.kinex.dto.SeatAdminDto;
import pl.termosteam.kinex.service.SeatService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import pl.termosteam.kinex.domain.Seat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"ADMINISTRATOR"})
@RunWith(SpringRunner.class)
class AdminAuditoriumSeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    private ObjectMapper mapper;
    private List<Seat> allSeat;

    @BeforeEach
    public void init(){
        allSeat = new ArrayList<>();
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Seat seat1 = new Seat(1, new Auditorium(), new Short("3"), new Short("10"), true, new ArrayList<>());
        Seat seat2 = new Seat(2, new Auditorium(), new Short("3"), new Short("10"), true, new ArrayList<>());
        Seat seat3 = new Seat(3, new Auditorium(), new Short("3"), new Short("10"), true, new ArrayList<>());
        allSeat.add(seat1);
        allSeat.add(seat2);
        allSeat.add(seat3);
    }

    @Test
    void addSeat() throws Exception {
        Seat seat1 = allSeat.get(0);
        when(seatService.addSeat(Mockito.any(Seat.class), Mockito.anyInt())).thenReturn(seat1);
        SeatAdminDto seatAdminDto = new SeatAdminDto(1, 3,10,true);
        MvcResult result = mockMvc.perform(post("/api/admin/auditorium/1/seat").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(seatAdminDto))).andExpect(status().isOk()).andReturn();
        SeatAdminDto response = mapper.readValue(result.getResponse().getContentAsString(), SeatAdminDto.class);
        Assert.assertEquals(seatAdminDto.getSeatNumber(), response.getSeatNumber());
        Assert.assertEquals(seatAdminDto.getSeatRow(), response.getSeatRow());
        Assert.assertEquals(seatAdminDto.getSeatActive(), response.getSeatActive());
    }

    @Test
    void deactivateSeat() throws Exception {
        String result = "The seat has been deactivated.";
        when(seatService.deactivateSeat(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(put("/api/admin/auditorium/seat/1/deactivate").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    void reactivateSeat() throws Exception {
        String result = "The seat has been reactivated.";
        when(seatService.reactivateSeat(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(put("/api/admin/auditorium/seat/1/reactivate").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    void deleteSeat() throws Exception {
        String result = "The seat has been deleted.";
        when(seatService.deleteSeat(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(delete("/api/admin/auditorium/seat/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }
}