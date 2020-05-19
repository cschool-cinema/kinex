package pl.termosteam.kinex.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinToCurrentYearPlusValidatorTest {

    private MinToCurrentYearPlusValidator minToCurrentYearPlusValidator;
    private Short input;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void init() {
        short min=1800;
        short years=2000;
        minToCurrentYearPlusValidator=new MinToCurrentYearPlusValidator();
        minToCurrentYearPlusValidator.setMinForTest(min);
        minToCurrentYearPlusValidator.setYearsForTest(years);
    }

    @Test
    public void isValidTest1() {
        input=new Short("2010");
        boolean res=minToCurrentYearPlusValidator.isValid(input,context);
        assertEquals(true, res);
    }

    @Test
    public void isValidTest2() {
        input=new Short("1700");
        boolean res=minToCurrentYearPlusValidator.isValid(input,context);
        assertEquals(false, res);
    }

}
