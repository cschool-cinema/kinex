package pl.termosteam.kinex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.termosteam.kinex.configuration.jwt.JwtToken;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.repository.UserRepository;
import pl.termosteam.kinex.validation.DateValidation;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private DateValidation dateValidationMock;
    @Mock
    private JwtToken jwtTokenUtilMock;
    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private UserRequestDto userRequestDto;

    @BeforeEach
    public void init() {
        user1 = new User("first", "first_last", "user1", "@email1", "password", "salt", Role.USER.getRole(),
                "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
        user2 = new User("second", "second_last", "username2", "@email2", "password", "salt",
                Role.ADMINISTRATOR.getRole(), "12323", true, true, LocalDateTime.now(), LocalDateTime.now());
        userRequestDto = new UserRequestDto("first1","last1","user1","email1","pass1");
    }

    @Test
    public void loadUserByUsernameOrEmailTest1() {
        given(userRepositoryMock.findByUsername(anyString())).willReturn(null);
        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsernameOrEmail(user1.getUsername()));
    }

    @Test
    public void loadUserByUsernameOrEmailTest2() {
        given(userRepositoryMock.findByUsername(anyString())).willReturn(user1);
        assertThrows(
                ValidationException.class,
                () -> userService.loadUserByUsernameOrEmail(user1.getUsername()));
    }

    @Test
    public void addUserWithRole() {
        given(jwtTokenUtilMock.generateActivationToken(any(),any())).willReturn("token");
        given(userRepositoryMock.save(user1)).willReturn(user1);
        Optional<User> res = userService.addUserWithRole(Role.OWNER,userRequestDto);
        assertEquals(Optional.of(user1),res);
    }

    @Test
    public void ifUsernameAlreadyExists() {
        given(userRepositoryMock.existsByUsername(user1.getUsername())).willReturn(true);
        boolean res = userService.ifUsernameAlreadyExists(user1.getUsername());
        assertEquals(true, res);
    }

    @Test
    public void ifEmailAlreadyExists() {
        given(userRepositoryMock.existsByEmail(user1.getEmail())).willReturn(true);
        boolean res = userService.ifEmailAlreadyExists(user1.getEmail());
        assertEquals(true, res);
    }

}
