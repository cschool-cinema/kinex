package pl.termosteam.kinex.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.Crypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.termosteam.kinex.configuration.JwtToken;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.JwtRequestDto;
import pl.termosteam.kinex.dto.JwtResponseDto;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.service.UserService;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class AuthenticateController {

    private final JwtToken jwtToken;
    private final UserService userService;

    @PostMapping(value = "authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest) {
        final User user = userService.loadUserByUsernameOrEmail(authenticationRequest.getUsername());

        if (Crypt.crypt(authenticationRequest.getPassword(), user.getSalt()).equals(user.getPassword())) {
            final String token = jwtToken.generateToken(user);
            return ResponseEntity.ok(new JwtResponseDto(token));
        }

        throw new ValidationException("authentication problems: INVALID_CREDENTIALS");
    }
}
