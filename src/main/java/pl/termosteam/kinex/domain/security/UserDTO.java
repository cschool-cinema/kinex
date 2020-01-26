package pl.termosteam.kinex.domain.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -2173097135422789376L;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}
