package pl.termosteam.kinex;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

@Configuration
public class ApplicationConfigTest {

    @Bean
    public ConversionService conversionService() {
        return ApplicationConversionService.getSharedInstance();
    }
}
