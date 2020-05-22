package pl.termosteam.kinex.controller.administration.accounts.register;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.dto.JwtActivationRequestDto;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@AllArgsConstructor
@Slf4j
public class ActivateController {

    private final UserService userService;

    @PostMapping(value = "activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateUserAccount(
            @RequestBody @Valid JwtActivationRequestDto authenticationRequest) {
        userService.activateByToken(authenticationRequest.getUsername(),
                authenticationRequest.getActivationToken());
        log.info("ActivateController->activateUserAccount: account activated for the username: " + authenticationRequest.getUsername());
    }
}
