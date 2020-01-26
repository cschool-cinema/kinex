package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.dto.UserDto;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserService userService;

    public void addOwnerRoleUser(UserDto userDTO) {
        userService.addUserWithRole(Role.OWNER, userDTO);
    }

    public void addAdministratorRoleUser(UserDto userDTO) {
        userService.addUserWithRole(Role.ADMINISTRATOR, userDTO);
    }

    public void addManagerRoleUser(UserDto userDTO) {
        userService.addUserWithRole(Role.MANAGER, userDTO);
    }

    public void addUserRoleUser(UserDto userDTO) {
        userService.addUserWithRole(Role.USER, userDTO);
    }

    public void addGuestUser(UserDto userDTO) {
        userService.addUserWithRole(Role.GUEST, userDTO);
    }

    public boolean checkOwnerAlreadyRegistered() {
        return userService.ifOwnerAlreadyExists();
    }
}
