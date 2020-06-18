package pl.termosteam.kinex.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import pl.termosteam.kinex.domain.*;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.service.RegisterService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @MockBean
    private RegisterService registerService;
    private UserRequestDto userRequestDto;

    @BeforeEach
    public void init(){
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        userRequestDto = new UserRequestDto("Cristiano", "ronaldo", "Cristiano",
                "Cristiano.007@gmail.com","Cristiano.Ronaldo@07");
    }

    @Test
    void createOwner() throws Exception {
        String result = "DEVELOPER MODE: User is registered and activated automatically.";
        when(registerService.registerUserWithRole(Mockito.any(Role.class), Mockito.any(UserRequestDto.class))).thenReturn(result);
        mockMvc.perform(post("/api/register/owner").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequestDto))).andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    @WithMockUser(roles = {"OWNER"})
    void createNextOwner() throws Exception {
        String result = "You account with the username \"" + "Cristiano" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "Cristiano.007@gmail.com";

        when(registerService.registerUserWithRole(Mockito.any(Role.class), Mockito.any(UserRequestDto.class))).thenReturn(result);
        mockMvc.perform(post("/api/register/owner/next").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequestDto))).andExpect(status().isCreated()).andExpect(content().string(result));
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    void createAdministrator() throws Exception {
        String result = "You account with the username \"" + "Cristiano" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "Cristiano.007@gmail.com";

        when(registerService.registerUserWithRole(Mockito.any(Role.class), Mockito.any(UserRequestDto.class))).thenReturn(result);
        mockMvc.perform(post("/api/register/administrator").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequestDto))).andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void createManager() throws Exception {
        String result = "You account with the username \"" + "Cristiano" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "Cristiano.007@gmail.com";
        when(registerService.registerUserWithRole(Mockito.any(Role.class), Mockito.any(UserRequestDto.class))).thenReturn(result);
        mockMvc.perform(post("/api/register/manager").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequestDto))).andExpect(status().isOk()).andExpect(content().string(result));
    }

    @Test
    @WithMockUser(roles = {"GUEST"})
    void createUser() throws Exception {
        String result = "You account with the username \"" + "Cristiano" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "Cristiano.007@gmail.com";

        when(registerService.registerUserWithRole(Mockito.any(Role.class), Mockito.any(UserRequestDto.class))).thenReturn(result);
        mockMvc.perform(post("/api/register/user").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userRequestDto))).andExpect(status().isOk()).andExpect(content().string(result));
    }
}