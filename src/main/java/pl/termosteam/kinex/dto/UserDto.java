package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import pl.termosteam.kinex.validation.CustomPassword;
import pl.termosteam.kinex.validation.StringCheckByRegex;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class UserDto {
    @StringCheckByRegex(patternRegex = "${validation.name.pattern}")
    private String firstName;
    @StringCheckByRegex(patternRegex = "${validation.name.pattern}")
    private String lastName;
    @StringCheckByRegex(patternRegex = "${validation.username.pattern}")
    private String username;
    @Email
    private String email;
    @CustomPassword
    private String password;
}
