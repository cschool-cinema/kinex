package pl.termosteam.kinex.validation;

import lombok.RequiredArgsConstructor;
import pl.termosteam.kinex.configuration.properties.RegexPatternConfiguration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UsernameCheckByRegexValidator implements ConstraintValidator<UsernameCheckByRegex, String> {
    private final RegexPatternConfiguration regexPatternConfiguration;
    private Pattern compile;

    @Override
    public void initialize(UsernameCheckByRegex constraintAnnotation) {
        compile = Pattern.compile(regexPatternConfiguration.getUsernamePattern());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return compile.matcher(s).matches();
    }
}
