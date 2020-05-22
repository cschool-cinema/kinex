package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class RegexPatternConfiguration {
    @Value("${validation.pattern.name.regexp}")
    private String namePattern;
    @Value("${validation.pattern.username.regexp}")
    private String usernamePattern;
}
