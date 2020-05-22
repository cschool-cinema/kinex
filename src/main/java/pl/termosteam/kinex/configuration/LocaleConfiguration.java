package pl.termosteam.kinex.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
@Getter
@RequiredArgsConstructor
public class LocaleConfiguration {

    private final ApplicationProperties applicationProperties;
    private final Environment env;

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(applicationProperties.getTimezone()));
    }

}
