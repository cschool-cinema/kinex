package pl.termosteam.kinex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserDto;
import pl.termosteam.kinex.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

@Service
public class RegisterService {

    private final UserService userService;
    private final Map<Role, Callable<Optional<User>>> routeRegistrationMap;
    private UserDto userDto;

    @Value("${development.return.activation.token}")
    private Boolean isReturnActivationToken;

    public RegisterService(UserService userService) {
        this.userService = userService;
        this.routeRegistrationMap = new HashMap<>();
        routeRegistrationMap.put(Role.OWNER, () -> userService.addUserWithRole(Role.OWNER, userDto));
        routeRegistrationMap.put(Role.ADMINISTRATOR, () -> userService.addUserWithRole(Role.ADMINISTRATOR, userDto));
        routeRegistrationMap.put(Role.MANAGER, () -> userService.addUserWithRole(Role.MANAGER, userDto));
        routeRegistrationMap.put(Role.USER, () -> userService.addUserWithRole(Role.USER, userDto));
        routeRegistrationMap.put(Role.GUEST, () -> userService.addUserWithRole(Role.GUEST, userDto));
    }

    public String registerUserWithRole(Role ROLE, UserDto userDTO) {

        if (ROLE.equals(Role.OWNER) && !ownerIsAuthorised()) {
            if (checkOwnerAlreadyRegistered()) {
                throw new ValidationException("Owner is already registered, if you want to register next owner, please login as owner.");
            }
        }

        if (!checkOwnerAlreadyRegistered()) {
            throw new ValidationException(
                    "Owner is not registered, if you want to register user with " + ROLE + " first register OWNER role");
        }

        this.userDto = userDTO;
        User user = (User) routeRegistrationMap.get(ROLE);
        if (isReturnActivationToken) {
            userService.activateByToken(user.getUsername(), user.getInMemoryActivationToken());
            return "DEVELOPER MODE: User is registered and activated automatically.";
        } else {
            return "You account with the username \"" + user.getUsername() +
                    "\" has been registered, please activate account using the token sent to the provided email: " +
                    user.getEmail();
        }
    }

    public boolean checkOwnerAlreadyRegistered() {
        return userService.ifOwnerAlreadyExists();
    }

    private boolean ownerIsAuthorised() {
        User user = userService.getUserNotNullIfAuthenticated();
        return user != null && user.getRole().equals(Role.OWNER.toString());
    }
}
