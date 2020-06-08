package pl.termosteam.kinex.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class StringCheckByRegexValidatorTest {

    private StringCheckByRegexValidator stringCheckByRegexValidator;

    @BeforeEach
    public void init() {
        stringCheckByRegexValidator=new StringCheckByRegexValidator();
        Pattern compile=Pattern.compile("[a-z]*[A-Z]*");
        stringCheckByRegexValidator.setCompile(compile);
    }

    @Test
    public void isValidTest1() {
        String stringToValidate="input";
        boolean res=stringCheckByRegexValidator.isValid(stringToValidate,null);
        assertTrue(res);
    }

    @Test
    public void isValidTest2() {
        String stringToValidate="123";
        boolean res=stringCheckByRegexValidator.isValid(stringToValidate,null);
        assertFalse(res);
    }
}
