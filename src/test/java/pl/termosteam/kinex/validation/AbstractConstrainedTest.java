package pl.termosteam.kinex.validation;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import pl.termosteam.kinex.ApplicationConfigTest;
import pl.termosteam.kinex.configuration.properties.*;


@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@ContextConfiguration(classes = {ApplicationConfigTest.class,JwtConfiguration.class, DeveloperConfiguration.class, ApplicationProperties.class, PasswordPatternConfiguration.class, RegexPatternConfiguration.class, StringCheckByRegexValidator.class})
public abstract class AbstractConstrainedTest {

    protected LocalValidatorFactoryBean validator;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @BeforeEach
    public void init() {
        SpringConstraintValidatorFactory springConstraintValidatorFactory
                = new SpringConstraintValidatorFactory(
                applicationContext.getAutowireCapableBeanFactory());
        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory
                (springConstraintValidatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();
    }

}
