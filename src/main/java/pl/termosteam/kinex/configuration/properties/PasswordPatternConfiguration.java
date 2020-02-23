package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class PasswordPatternConfiguration {
    @Value("${validation.password.length.max.rule}")
    private int lengthMaxRule;
    @Value("${validation.password.length.min.rule}")
    private int lengthMinRule;
    @Value("${validation.password.uppercase.character.rule}")
    private int uppercaseCharacterRule;
    @Value("${validation.password.digit.character.rule}")
    private int digitCharacterRule;
    @Value("${validation.password.special.character.rule}")
    private int specialCharacterRule;
    @Value("${validation.password.numerical.sequence.rule}")
    private int numericalSequenceRule;
    @Value("${validation.password.alphabetical.sequence.rule}")
    private int alphabeticalSequenceRule;
    @Value("${validation.password.qwerty.sequence.rule}")
    private int qwertySequenceRule;
}
