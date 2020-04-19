package pl.termosteam.kinex.configuration.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@ConfigurationProperties(prefix = "validation")
public class PasswordPatternConfiguration {

    @Value("${validation.password.max.length}")
    private int maxLength;
    @Value("${validation.password.min.length}")
    private int minLength;
    @Value("${validation.password.min.uppercase.character}")
    private int minNumberOfUppercaseCharacters;
    @Value("${validation.password.min.digit.character}")
    private int minNumberOfDigitCharacters;
    @Value("${validation.password.min.special.character}")
    private int minNumberOfSpecialCharacters;
    @Value("${validation.password.numerical.sequence}")
    private int numberOfCharactersInNumericalSequence;
    @Value("${validation.password.alphabetical.sequence}")
    private int numberOfCharactersInAlphabeticalSequenceRule;
    @Value("${validation.password.qwerty.sequence}")
    private int numberOfCharactersInQwertySequenceRule;
}
