package pl.termosteam.kinex.domain.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtActivationRequest implements Serializable {
    private static final long serialVersionUID = -1217734736382435074L;
    private String username;
    private String activation_token;
}
