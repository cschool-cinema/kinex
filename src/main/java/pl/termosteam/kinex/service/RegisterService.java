package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.configuration.properties.DeveloperConfiguration;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.exception.ValidationException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserService userService;
    private final DeveloperConfiguration developerConfiguration;

    public String registerUserWithRole(Role ROLE, UserRequestDto userRequestDTO) {

        if (ROLE.equals(Role.OWNER) && !ownerIsAuthorised()) {
            if (checkOwnerAlreadyRegistered()) {
                throw new ValidationException("Owner is already registered, if you want to register next owner, please login as owner.");
            }
        }

        if (!checkOwnerAlreadyRegistered() && !ROLE.equals(Role.OWNER)) {
            throw new ValidationException(
                    "Owner is not registered, if you want to register user with " + ROLE + " first register OWNER role");
        }

        Optional<User> user = addUserRoleSelection(ROLE, userRequestDTO);

        if (developerConfiguration.getIsReturnActivationToken()) {
            userService.activateByToken(user.get().getUsername(), user.get().getInMemoryActivationToken());
            return "DEVELOPER MODE: User is registered and activated automatically.";
        } else {
            return "You account with the username \"" + user.get().getUsername() +
                    "\" has been registered, please activate account using the token sent to the provided email: " +
                    user.get().getEmail();
        }
    }

    private Optional<User> addUserRoleSelection(Role ROLE, UserRequestDto userRequestDto) {
        switch (ROLE) {
            case OWNER:
                return userService.addUserWithRole(Role.OWNER, userRequestDto);
            case ADMINISTRATOR:
                return userService.addUserWithRole(Role.ADMINISTRATOR, userRequestDto);
            case MANAGER:
                return userService.addUserWithRole(Role.MANAGER, userRequestDto);
            case USER:
                return userService.addUserWithRole(Role.USER, userRequestDto);
            case GUEST:
                return userService.addUserWithRole(Role.GUEST, userRequestDto);
            default:
                return null;
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
