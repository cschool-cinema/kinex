package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class JwtConfiguration {

    @Value("${jwt.token.validity.time.minutes:180000}")
    private int JWT_TOKEN_VALIDITY_IN_MIN;

    @Value("${jwt.token.validity.activation.time.minutes}")
    private int JWT_TOKEN_VALIDITY_ACTIVATION_TIME_IN_MIN;

    @Value("${jwt.secret}")
    private String SECRET;
}
