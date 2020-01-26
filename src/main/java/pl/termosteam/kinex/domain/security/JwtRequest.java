package pl.termosteam.kinex.domain.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 4016905276052333730L;
    private String username;
    private String password;
}

