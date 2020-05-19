package pl.termosteam.kinex.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import pl.termosteam.kinex.controller.administration.accounts.register.RegisterController;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.service.RegisterService;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private RegisterService registerService;
    @Mock
    private ModelMapper mm;
    @InjectMocks
    private RegisterController registerController;

    private UserRequestDto userRequestDto;
    private Role role;
    private User user;

    @BeforeEach
    public void init(){
        userRequestDto = new UserRequestDto();
        role = Role.OWNER;
        user = new User("firstName", "lastName", "username", "email", "password", "salt", Role.USER.getRole(), "12323",
                true, true, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createOwner() {
        String result = "DEVELOPER MODE: User is registered and activated automatically.";
        when(registerService.registerUserWithRole(role, userRequestDto)).thenReturn(result);
        String createOwnerResult = registerController.createOwner(userRequestDto);
        assertEquals(result, createOwnerResult);
    }

    @Test
    void createNextOwner() {
        String result = "You account with the username \"" + "user" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "user@123";

        when(registerService.registerUserWithRole(role, userRequestDto)).thenReturn(result);
        String createNextOwnerResult = registerController.createNextOwner(userRequestDto);
        assertEquals(result, createNextOwnerResult);
    }

    @Test
    void createAdministrator() {
        String result = "You account with the username \"" + "user" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "user@123";

        when(registerService.registerUserWithRole(Role.ADMINISTRATOR, userRequestDto)).thenReturn(result);
        ResponseEntity<String>  createAdministrator = registerController.createAdministrator(userRequestDto);
        assertEquals(result,createAdministrator.getBody());
    }

    @Test
    void createManager() {
       String result = "You account with the username \"" + "user1" +
               "\" has been registered, please activate account using the token sent to the provided email: " +
               "user1@123";

        when(registerService.registerUserWithRole(Role.MANAGER, userRequestDto)).thenReturn(result);
        ResponseEntity<String>  createManager = registerController.createManager(userRequestDto);
        assertEquals(result,createManager.getBody());
    }

    @Test
    void createUser() {
        String result = "You account with the username \"" + "user2" +
                "\" has been registered, please activate account using the token sent to the provided email: " +
                "user2@123";

        when(registerService.registerUserWithRole(Role.USER, userRequestDto)).thenReturn(result);
        ResponseEntity<String>  createUser = registerController.createUser(userRequestDto);
        assertEquals(result,createUser.getBody());
    }
}