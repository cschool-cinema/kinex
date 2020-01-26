package pl.termosteam.kinex.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.security.UserDTO;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.service.security.RegistrationService;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("kinex/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/owner")
    public Boolean createOwner(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
        if (registrationService.checkOwnerAlreadyRegistered()) {
            throw new ValidationException("Owner already registered, if you want to register ");
        }
        registrationService.addOwnerRoleUser(userDTO);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/administrator")
    public Boolean createAdministrator(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
        if (!registrationService.checkOwnerAlreadyRegistered()) {
            throw new ValidationException(
                    "Owner not registered, if you want to register administrator first register owner");
        }
        registrationService.addAdministratorRoleUser(userDTO);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/manager")
    public Boolean createManager(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
        registrationService.addManagerRoleUser(userDTO);
        return true;
    }

    @PostMapping("/user")
    public Boolean createUser(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
        registrationService.addUserRoleUser(userDTO);
        return true;
    }
}
