package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@EnableConfigurationProperties(EmailProperties.class)
@Getter
@RequiredArgsConstructor
public class ApplicationProperties {
    @Value("${application.timezone}")
    private String timezone;

    private final EmailProperties emailProperties;
    private final DeveloperConfiguration developerConfiguration;
    private final JwtConfiguration jwtConfiguration;
    private final PasswordPatternConfiguration passwordPatternConfiguration;
    private final RegexPatternConfiguration regexPatternConfiguration;

}
