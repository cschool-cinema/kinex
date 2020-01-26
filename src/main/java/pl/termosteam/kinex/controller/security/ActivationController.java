package pl.termosteam.kinex.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import pl.termosteam.kinex.configuration.JwtToken;
import pl.termosteam.kinex.domain.security.JwtActivationRequest;
import pl.termosteam.kinex.service.security.UserServiceImplementation;

@RestController
@CrossOrigin
public class ActivationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserServiceImplementation userServiceImplementation;


    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public ResponseEntity<?> activateUserAccount(
            @RequestBody JwtActivationRequest authenticationRequest) throws Exception {
        userServiceImplementation.activateByToken(authenticationRequest.getUsername(),
                authenticationRequest.getActivation_token());
        return ResponseEntity.ok("Account Activated");
    }

}
