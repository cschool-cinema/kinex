package pl.termosteam.kinex.controller.administration.accounts;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Crypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.configuration.jwt.JwtToken;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.JwtRequestDto;
import pl.termosteam.kinex.dto.JwtResponseDto;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.service.UserService;

import java.util.Date;

@RestController
@RequestMapping("api")
@AllArgsConstructor
@Slf4j
public class AuthenticateController {

    private final JwtToken jwtToken;
    private final UserService userService;

    @PostMapping(value = "authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest) {
        final User user = userService.loadUserByUsernameOrEmail(authenticationRequest.getUsername());
        if (Crypt.crypt(authenticationRequest.getPassword(), user.getSalt()).equals(user.getPassword())) {
            final String token = jwtToken.generateAuthenticationToken(user, new Date(System.currentTimeMillis()));
            log.info("AuthenticateController->createAuthenticationToken: has been generated for user " + user.getUsername());
            return ResponseEntity.ok(new JwtResponseDto(token));
        }
        throw new ValidationException("authentication problems: INVALID_CREDENTIALS");
    }


    @PostMapping(value = "authenticate/guest")
    public ResponseEntity<?> createAuthenticationGuestToken() {

        final User user = userService.loadUserByUsernameOrEmail("guest");
        final String token = jwtToken.generateAuthenticationToken(user, new Date(System.currentTimeMillis()));
        log.info("AuthenticateController->createAuthenticationToken: has been generated for user " + user.getUsername());
        return ResponseEntity.ok(new JwtResponseDto(token));

    }
}
