package pl.termosteam.kinex.validation;

import lombok.RequiredArgsConstructor;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UsernameCheckByRegexValidator implements ConstraintValidator<UsernameCheckByRegex, String> {
    private final ApplicationProperties applicationProperties;
    private Pattern compile;

    @Override
    public void initialize(UsernameCheckByRegex constraintAnnotation) {
        compile = Pattern.compile(applicationProperties.getRegexPatternConfig().getUsernamePattern());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return compile.matcher(s).matches();
    }
}
