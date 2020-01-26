package pl.termosteam.kinex.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.domain.security.Role;
import pl.termosteam.kinex.domain.security.UserDTO;

@Service
public class RegistrationService {
    private final UserService userService;

    @Autowired
    public RegistrationService(UserService userService) {
        this.userService = userService;
    }

    public void addOwnerRoleUser(UserDTO userDTO) {
        userService.addUserWithRole(Role.OWNER, userDTO);
    }

    public void addAdministratorRoleUser(UserDTO userDTO) {
        userService.addUserWithRole(Role.ADMINISTRATOR, userDTO);
    }

    public void addManagerRoleUser(UserDTO userDTO) {
        userService.addUserWithRole(Role.MANAGER, userDTO);
    }

    public void addUserRoleUser(UserDTO userDTO) {
        userService.addUserWithRole(Role.USER, userDTO);
    }

    public void addGuestUser(UserDTO userDTO) {
        userService.addUserWithRole(Role.GUEST, userDTO);
    }

    public boolean checkOwnerAlreadyRegistered() {
        return userService.ifOwnerAlreadyExists();
    }
}
