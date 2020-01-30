package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.dto.UserDto;
import pl.termosteam.kinex.service.RegisterService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/register")
@AllArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("owner")
    public ResponseEntity<String> createOwner(@RequestBody @Valid UserDto userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.OWNER, userDTO));
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/owner/next")
    public ResponseEntity<String> createNextOwner(@RequestBody @Valid UserDto userDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.OWNER, userDTO));
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("administrator")
    public ResponseEntity<String> createAdministrator(@RequestBody @Valid UserDto userDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.ADMINISTRATOR, userDTO));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("manager")
    public ResponseEntity<String> createManager(@RequestBody @Valid UserDto userDTO) {
        registerService.registerUserWithRole(Role.MANAGER, userDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.MANAGER, userDTO));
    }

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDto userDTO) {
        registerService.registerUserWithRole(Role.USER, userDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.USER, userDTO));
    }
}
