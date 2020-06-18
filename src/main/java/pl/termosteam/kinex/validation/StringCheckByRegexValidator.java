package pl.termosteam.kinex.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StringCheckByRegexValidator implements ConstraintValidator<StringCheckByRegex, String> {
    private String patternRegex;
    private Pattern compile;

    @Override
    public void initialize(StringCheckByRegex constraintAnnotation) {
        this.patternRegex = constraintAnnotation.patternRegex();
        compile = Pattern.compile(patternRegex);
    }

    @Override
    public boolean isValid(String stringToValidate, ConstraintValidatorContext constraintValidatorContext) {
        return compile.matcher(stringToValidate).matches();
    }

    //setter for test
    void setCompile(Pattern compile){
        this.compile=compile;
    }
}
