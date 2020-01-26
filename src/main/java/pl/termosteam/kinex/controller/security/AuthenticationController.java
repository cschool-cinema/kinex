package pl.termosteam.kinex.controller.security;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.configuration.JwtToken;
import pl.termosteam.kinex.domain.security.JwtRequest;
import pl.termosteam.kinex.domain.security.JwtResponse;
import pl.termosteam.kinex.domain.security.User;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.service.security.UserServiceImplementation;

@RestController
@CrossOrigin

public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtToken jwtToken;

    private final UserServiceImplementation userServiceImplementation;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtToken jwtToken,
                                    UserServiceImplementation userServiceImplementation) {
        this.authenticationManager = authenticationManager;
        this.jwtToken = jwtToken;
        this.userServiceImplementation = userServiceImplementation;
    }


    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        final User user = userServiceImplementation.loadUserByUsernameOrEmail(authenticationRequest.getUsername());

        if (Crypt.crypt(authenticationRequest.getPassword(), user.getSalt()).equals(user.getPassword())) {
            final String token = jwtToken.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        }

        throw new ValidationException("authentication problems: INVALID_CREDENTIALS");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
