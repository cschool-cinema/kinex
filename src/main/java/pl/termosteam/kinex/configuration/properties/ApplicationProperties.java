package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
@Getter
public class ApplicationProperties {
    @Value("${application.timezone}")
    private String timezone;
}
