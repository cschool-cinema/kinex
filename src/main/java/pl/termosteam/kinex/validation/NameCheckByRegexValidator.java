package pl.termosteam.kinex.validation;

import lombok.RequiredArgsConstructor;
import pl.termosteam.kinex.configuration.properties.RegexPatternConfiguration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class NameCheckByRegexValidator implements ConstraintValidator<NameCheckByRegex, String> {
    private final RegexPatternConfiguration regexPatternConfiguration;
    private Pattern compile;

    @Override
    public void initialize(NameCheckByRegex constraintAnnotation) {
        compile = Pattern.compile(regexPatternConfiguration.getNamePattern());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return compile.matcher(s).matches();
    }
}