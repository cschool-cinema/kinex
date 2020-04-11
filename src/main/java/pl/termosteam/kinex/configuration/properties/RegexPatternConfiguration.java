package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class RegexPatternConfiguration {
    @Value("${validation.regexp.pattern.name}")
    private String namePattern;
    @Value("${validation.regexp.pattern.username}")
    private String usernamePattern;
}
