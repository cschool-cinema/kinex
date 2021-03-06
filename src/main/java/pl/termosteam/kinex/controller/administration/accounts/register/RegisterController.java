package pl.termosteam.kinex.controller.administration.accounts.register;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.service.RegisterService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/register")
@AllArgsConstructor
@Slf4j
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("owner")
    public String createOwner(@RequestBody @Valid UserRequestDto userRequestDTO) {
        log.trace("@PostMapping(\"owner\"): starting");
        return registerService.registerUserWithRole(Role.OWNER, userRequestDTO);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("owner/next")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNextOwner(@RequestBody @Valid UserRequestDto userRequestDTO) {
        log.trace("@PostMapping(\"owner/next\"): starting");
        return registerService.registerUserWithRole(Role.OWNER, userRequestDTO);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("administrator")
    public ResponseEntity<String> createAdministrator(@RequestBody @Valid UserRequestDto userRequestDTO) {
        log.trace("@PostMapping(\"administrator\"): starting");
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.ADMINISTRATOR, userRequestDTO));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("manager")
    public ResponseEntity<String> createManager(@RequestBody @Valid UserRequestDto userRequestDTO) {
        log.trace("@PostMapping(\"manager\"): starting");
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.MANAGER, userRequestDTO));
    }

    @PreAuthorize("hasRole('GUEST')")
    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDto userRequestDTO) {
        log.trace("@PostMapping(\"user\"): starting");
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.USER, userRequestDTO));
    }
}
