package pl.termosteam.kinex.controller.administration.accounts.register;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.dto.JwtActivationRequestDto;
import pl.termosteam.kinex.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class ActivateController {

    private final Logger logger = LoggerFactory.getLogger(ActivateController.class);
    private final UserService userService;

    @PostMapping(value = "activate")
    public ResponseEntity<?> activateUserAccount(
            @RequestBody @Valid JwtActivationRequestDto authenticationRequest) {
        userService.activateByToken(authenticationRequest.getUsername(),
                authenticationRequest.getActivation_token());
        logger.info("ActivateController->activateUserAccount: account activated for the username: " + authenticationRequest.getUsername());
        return ResponseEntity.ok("Account Activated");
    }
}