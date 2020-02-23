package pl.termosteam.kinex.controller.administration.accounts.login;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.service.RegisterService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/register")
@AllArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("owner")
    public ResponseEntity<String> createOwner(@RequestBody @Valid UserRequestDto userRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.OWNER, userRequestDTO));
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("owner/next")
    public ResponseEntity<String> createNextOwner(@RequestBody @Valid UserRequestDto userRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.OWNER, userRequestDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("administrator")
    public ResponseEntity<String> createAdministrator(@RequestBody @Valid UserRequestDto userRequestDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.ADMINISTRATOR, userRequestDTO));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("manager")
    public ResponseEntity<String> createManager(@RequestBody @Valid UserRequestDto userRequestDTO) {
        registerService.registerUserWithRole(Role.MANAGER, userRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.MANAGER, userRequestDTO));
    }

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDto userRequestDTO) {
        registerService.registerUserWithRole(Role.USER, userRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.USER, userRequestDTO));
    }
}
