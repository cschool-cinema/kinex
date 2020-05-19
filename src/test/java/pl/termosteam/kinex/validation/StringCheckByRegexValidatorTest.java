package pl.termosteam.kinex.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringCheckByRegexValidatorTest {

    private StringCheckByRegexValidator stringCheckByRegexValidator;
    private String stringToValidate;
    private ConstraintValidatorContext context;
    private Pattern compile;

    @BeforeEach
    public void init() {
        stringCheckByRegexValidator=new StringCheckByRegexValidator();
        compile=Pattern.compile("[a-z]*[A-Z]*");
        stringCheckByRegexValidator.setCompile(compile);
    }

    @Test
    public void isValidTest1() {
        stringToValidate="input";
        boolean res=stringCheckByRegexValidator.isValid(stringToValidate,context);
        assertEquals(true, res);
    }

    @Test
    public void isValidTest2() {
        stringToValidate="123";
        boolean res=stringCheckByRegexValidator.isValid(stringToValidate,context);
        assertEquals(false, res);
    }
}
