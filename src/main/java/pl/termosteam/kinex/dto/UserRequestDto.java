package pl.termosteam.kinex.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import pl.termosteam.kinex.validation.CustomPassword;
import pl.termosteam.kinex.validation.NameCheckByRegex;
import pl.termosteam.kinex.validation.UsernameCheckByRegex;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class UserRequestDto {

    @NameCheckByRegex
    private String firstName;
    @NameCheckByRegex
    private String lastName;
    @UsernameCheckByRegex
    private String username;
    @Email
    private String email;
    @CustomPassword
    private String password;
}
