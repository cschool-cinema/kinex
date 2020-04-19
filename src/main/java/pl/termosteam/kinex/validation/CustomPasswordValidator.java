package pl.termosteam.kinex.validation;

import lombok.RequiredArgsConstructor;
import org.passay.*;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;
import pl.termosteam.kinex.configuration.properties.PasswordPatternConfiguration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@RequiredArgsConstructor
public class CustomPasswordValidator implements ConstraintValidator<CustomPassword, String> {

    private final ApplicationProperties applicationProperties;

    public void initialize(CustomPassword constraint) {
    }

    public boolean isValid(String password, ConstraintValidatorContext context) {

        PasswordPatternConfiguration configuration = applicationProperties.getPasswordPatternConfiguration();

        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(configuration.getMinLength(), configuration.getMaxLength()),
                new UppercaseCharacterRule(configuration.getMinNumberOfUppercaseCharacters()),
                new DigitCharacterRule(configuration.getMinNumberOfDigitCharacters()),
                new SpecialCharacterRule(configuration.getMinNumberOfSpecialCharacters()),
                new NumericalSequenceRule(configuration.getNumberOfCharactersInNumericalSequence(), false),
                new AlphabeticalSequenceRule(configuration.getNumberOfCharactersInAlphabeticalSequenceRule(), false),
                new QwertySequenceRule(configuration.getNumberOfCharactersInQwertySequenceRule(), false),
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(password));

        return result.isValid();
    }
}
