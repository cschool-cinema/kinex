package pl.termosteam.kinex.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StringCheckByRegexValidator implements ConstraintValidator<StringCheckByRegex, String> {
    private String patternRegex;
    private Pattern compile;

    public void initialize(StringCheckByRegex constraint) {
        this.patternRegex = constraint.patternRegex();
        compile = Pattern.compile(patternRegex);
    }

    public boolean isValid(String stringToValidate, ConstraintValidatorContext context) {
        return compile.matcher(stringToValidate).matches();
    }
}
