package pl.termosteam.kinex.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class DeveloperConfiguration {
    @Value("${development.return.activation.token}")
    private Boolean isReturnActivationToken;
}