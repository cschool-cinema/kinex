package pl.termosteam.kinex.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
import pl.termosteam.kinex.dto.ScreeningRequestDto;
import pl.termosteam.kinex.dto.ScreeningResponseDto;
import pl.termosteam.kinex.service.ScreeningService;

import java.time.LocalDateTime;
import java.util.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"MANAGER"})
class AdminScreeningControllerTest {

    @MockBean
    private ScreeningService screeningService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private List<Screening> allScreening;

    @BeforeEach
    public void init() {
        allScreening = new ArrayList<>();
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Screening screening1 = new Screening(11, new Movie(), new Auditorium(), LocalDateTime.of(2011, 4, 5, 12, 34),new ArrayList<>());
        Screening screening2 = new Screening(12, new Movie(), new Auditorium(), LocalDateTime.of(2014, 1, 3, 17, 36),new ArrayList<>());
        Screening  screening3 = new Screening(13, new Movie(), new Auditorium(), LocalDateTime.of(2019, 7, 2, 21, 23),new ArrayList<>());
        allScreening.add(screening1);
        allScreening.add(screening2);
        allScreening.add(screening3);
    }

    @Test
    void findAllScreenings() throws Exception {
        when(screeningService.findAllScreenings()).thenReturn(allScreening);
        MvcResult result = mockMvc.perform(get("/api/admin/screening").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        ScreeningResponseDto[] response = mapper.readValue(result.getResponse().getContentAsString(), ScreeningResponseDto[].class);
        Assert.assertEquals(response.length, allScreening.size());
    }

    @Test
    void findScreeningById() throws Exception {
        Screening findById = allScreening.get(1);
        when(screeningService.findScreeningById(12)).thenReturn(findById);
        MvcResult result = mockMvc.perform(get("/api/admin/screening/12").header("type","text").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        ScreeningResponseDto response = mapper.readValue(result.getResponse().getContentAsString(), ScreeningResponseDto.class);
        Assert.assertEquals(findById.getScreeningStart(), response.getScreeningStart());
    }

    @Test
    void deleteScreening() throws Exception {
        String result = "Screening has been successfully deleted.";
        when(screeningService.deleteScreening(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(delete("/api/admin/screening/12").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    void createScreening() throws Exception {
        Screening screening = allScreening.get(0);
        screening.getMovie().setReleaseYear(new Short("1900"));

        ScreeningRequestDto screeningRequestDto = new ScreeningRequestDto(LocalDateTime.now().plusDays(1), 1,1);
        when(screeningService.createScreening(Mockito.any(ScreeningRequestDto.class))).thenReturn(screening);
        MvcResult result = mockMvc.perform(post("/api/admin/screening").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(screeningRequestDto))).andExpect(status().isOk()).andReturn();

        ScreeningResponseDto response = mapper.readValue(result.getResponse().getContentAsString(), ScreeningResponseDto.class);
        Assert.assertEquals(screening.getScreeningStart(), response.getScreeningStart());
        Assert.assertEquals(screening.getAuditorium().getId(), response.getAuditoriumId());
        Assert.assertEquals(screening.getMovie().getId(), response.getMovieId());
    }

    @Test
    void updateScreening() throws Exception {
        Screening screening = allScreening.get(2);
        when(screeningService.createScreening(Mockito.any(ScreeningRequestDto.class))).thenReturn(screening);
        ScreeningRequestDto screeningRequestDto = new ScreeningRequestDto(LocalDateTime.now().plusDays(1), 1,1);
        when(screeningService.updateScreeningDetails(Mockito.any(ScreeningRequestDto.class), Mockito.anyInt())).thenReturn(screening);
        MvcResult result = mockMvc.perform(put("/api/admin/screening/12").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(screeningRequestDto))).andExpect(status().isOk()).andReturn();

        ScreeningResponseDto response = mapper.readValue(result.getResponse().getContentAsString(), ScreeningResponseDto.class);
        Assert.assertEquals(screening.getScreeningStart(), response.getScreeningStart());
        Assert.assertEquals(screening.getAuditorium().getId(), response.getAuditoriumId());
        Assert.assertEquals(screening.getMovie().getId(), response.getMovieId());
    }
}