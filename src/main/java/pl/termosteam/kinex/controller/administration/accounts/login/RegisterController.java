package pl.termosteam.kinex.controller.administration.accounts.login;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private final RegisterService registerService;

    @PostMapping("owner")
    public String createOwner(@RequestBody @Valid UserRequestDto userRequestDTO) {
        logger.trace("@PostMapping(\"owner\"): starting");
        return registerService.registerUserWithRole(Role.OWNER, userRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("owner/next")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNextOwner(@RequestBody @Valid UserRequestDto userRequestDTO) {
        logger.trace("@PostMapping(\"owner/next\"): starting");
        return registerService.registerUserWithRole(Role.OWNER, userRequestDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    @PostMapping("administrator")
    public ResponseEntity<String> createAdministrator(@RequestBody @Valid UserRequestDto userRequestDTO) {
        logger.trace("@PostMapping(\"administrator\"): starting");
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.ADMINISTRATOR, userRequestDTO));
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("manager")
    public ResponseEntity<String> createManager(@RequestBody @Valid UserRequestDto userRequestDTO) {
        logger.trace("@PostMapping(\"manager\"): starting");
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.MANAGER, userRequestDTO));
    }

    @PostMapping("user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDto userRequestDTO) {
        logger.trace("@PostMapping(\"user\"): starting");
        return ResponseEntity.status(HttpStatus.OK)
                .body(registerService.registerUserWithRole(Role.USER, userRequestDTO));
    }
}
