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
import pl.termosteam.kinex.dto.AuditoriumDto;
import pl.termosteam.kinex.service.AuditoriumService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = {"ADMINISTRATOR"})
@RunWith(SpringRunner.class)
class AdminAuditoriumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditoriumService auditoriumService;
    private List<Auditorium> allAuditorium;
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        allAuditorium = new ArrayList<>();
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Auditorium auditorium1 = new Auditorium(1, "David", true, new ArrayList<>(), new ArrayList<>());
        Auditorium auditorium2 = new Auditorium(2, "Jack", true, new ArrayList<>(), new ArrayList<>());
        Auditorium auditorium3 = new Auditorium(3, "Alen", true, new ArrayList<>(), new ArrayList<>());
        allAuditorium.add(auditorium1);
        allAuditorium.add(auditorium2);
        allAuditorium.add(auditorium3);
    }

    @Test
    void findAllAuditoriums() throws Exception {
        when(auditoriumService.findAllAuditoriums()).thenReturn(allAuditorium);
        MvcResult result = mockMvc.perform(get("/api/admin/auditorium").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        AuditoriumDto[] response = mapper.readValue(result.getResponse().getContentAsString(), AuditoriumDto[].class);
        Assert.assertEquals(response.length, allAuditorium.size());
    }

    @Test
    void findAuditoriumById() throws Exception {
        Auditorium findById = allAuditorium.get(1);
        when(auditoriumService.findAuditoriumById(Mockito.anyInt())).thenReturn(findById);
        MvcResult result = mockMvc.perform(get("/api/admin/auditorium/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        AuditoriumDto response = mapper.readValue(result.getResponse().getContentAsString(), AuditoriumDto.class);
        Assert.assertEquals(findById.getName(), response.getName());
        Assert.assertEquals(findById.getId(), response.getId());
        Assert.assertEquals(findById.getActive(), response.getActive());
    }

    @Test
    void createAuditorium() throws Exception {
        Auditorium auditorium = allAuditorium.get(0);
        when(auditoriumService.createAuditorium(Mockito.any(Auditorium.class))).thenReturn(auditorium);
        AuditoriumDto auditoriumDto = new AuditoriumDto(1, "David", true, null);
        MvcResult result = mockMvc.perform(post("/api/admin/auditorium").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(auditoriumDto)))
                .andExpect(status().isOk()).andReturn();
        AuditoriumDto response = mapper.readValue(result.getResponse().getContentAsString(), AuditoriumDto.class);
        Assert.assertEquals(auditoriumDto.getName(), response.getName());
        Assert.assertEquals(auditoriumDto.getId(), response.getId());
        Assert.assertEquals(auditoriumDto.getActive(), response.getActive());
        Assert.assertEquals(auditorium.getName(), response.getName());
        Assert.assertEquals(auditorium.getId(), response.getId());
        Assert.assertEquals(auditorium.getActive(), response.getActive());
    }

    @Test
    void deactivateAuditorium() throws Exception{
        String result = "The auditorium and all its seats have been deactivated.";
        when(auditoriumService.deactivateAuditorium(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(put("/api/admin/auditorium/2/deactivate").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    void reactivateAuditorium() throws Exception {
        String result = "The auditorium and all its seats have been reactivated.";
        when(auditoriumService.reactivateAuditorium(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(put("/api/admin/auditorium/2/reactivate").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    void deleteAuditorium() throws Exception {
        String result = "The auditorium and all its seats were removed from database.";
        when(auditoriumService.deleteAuditorium(Mockito.anyInt())).thenReturn(result);
        mockMvc.perform(delete("/api/admin/auditorium/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(result));
    }
}