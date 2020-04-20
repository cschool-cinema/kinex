package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Getter
public class JwtConfiguration {

    @Value("${jwt.token.validity.time.minutes:PT300M}")
    private Duration JWT_TOKEN_VALIDITY;

    @Value("${jwt.token.validity.activation.time.minutes:PT25M}")
    private Duration JWT_TOKEN_VALIDITY_ACTIVATION_TIME;

    @Value("${jwt.secret}")
    private String SECRET;
}
