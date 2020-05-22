package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DeveloperConfiguration {
    @Value("${development.return.activation.token}")
    private Boolean isReturnActivationToken;
}
