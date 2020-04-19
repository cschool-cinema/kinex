package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@Getter
public class JwtConfiguration {

    @Value("#{T(java.time.Duration).ofMinutes('${jwt.token.validity.time.minutes:300}')}")
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration JWT_TOKEN_VALIDITY;

    @Value("#{T(java.time.Duration).ofMinutes('${jwt.token.validity.activation.time.minutes:25}')}")
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration JWT_TOKEN_VALIDITY_ACTIVATION_TIME;

    @Value("${jwt.secret}")
    private String SECRET;
}
