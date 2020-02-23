package pl.termosteam.kinex.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StringCheckByRegexValidator implements ConstraintValidator<StringCheckByRegex, String> {
    private String patternRegex;

    public void initialize(StringCheckByRegex constraint) {
        this.patternRegex = constraint.patternRegex();
    }

    public boolean isValid(String stringToValidate, ConstraintValidatorContext context) {
        return Pattern.compile(patternRegex).matcher(stringToValidate).matches();
    }
}
