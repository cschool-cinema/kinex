package pl.termosteam.kinex.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.service.UserService;

import javax.annotation.PostConstruct;

@Configuration
@Getter
@RequiredArgsConstructor
public class GuestAccountInitialisation {
    private final UserService userService;

    @PostConstruct
    void initGuestUser() {
        if (userService.ifUserNotExists("guest")) {
            userService.activateGuest(userService.addUserWithRole(Role.GUEST,
                    new UserRequestDto("guest",
                            "guest",
                            "guest",
                            "guest@email.com",
                            "guest")).get());
        }
    }
}
