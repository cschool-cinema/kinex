package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.dto.UserDto;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.service.RegisterService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/register")
@AllArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("owner")
    public boolean createOwner(@RequestBody @Valid UserDto userDTO) {
        if (registerService.checkOwnerAlreadyRegistered()) {
            throw new ValidationException("Owner already registered, if you want to register ");
        }
        registerService.addOwnerRoleUser(userDTO);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("administrator")
    public Boolean createAdministrator(@RequestBody @Valid UserDto userDTO) {
        if (!registerService.checkOwnerAlreadyRegistered()) {
            throw new ValidationException(
                    "Owner not registered, if you want to register administrator first register owner");
        }
        registerService.addAdministratorRoleUser(userDTO);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("manager")
    public Boolean createManager(@RequestBody @Valid UserDto userDTO) {
        registerService.addManagerRoleUser(userDTO);
        return true;
    }

    @PostMapping("user")
    public Boolean createUser(@RequestBody @Valid UserDto userDTO) {
        registerService.addUserRoleUser(userDTO);
        return true;
    }
}
