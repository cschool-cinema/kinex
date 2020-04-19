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
                new LengthRule(configuration.getLengthMinRule(), configuration.getLengthMaxRule()),
                new UppercaseCharacterRule(configuration.getUppercaseCharacterRule()),
                new DigitCharacterRule(configuration.getDigitCharacterRule()),
                new SpecialCharacterRule(configuration.getSpecialCharacterRule()),
                new NumericalSequenceRule(configuration.getNumericalSequenceRule(), false),
                new AlphabeticalSequenceRule(configuration.getAlphabeticalSequenceRule(), false),
                new QwertySequenceRule(configuration.getQwertySequenceRule(), false),
                new WhitespaceRule()));
        RuleResult result = validator.validate(new PasswordData(password));

        return result.isValid();
    }
}
