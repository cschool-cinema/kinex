package pl.termosteam.kinex.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NowToPlusDaysValidatorTest {

    private NowToPlusDaysValidator nowToPlusDaysValidator;
    private LocalDateTime localDateTime;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void init() {
        nowToPlusDaysValidator=new NowToPlusDaysValidator();
        nowToPlusDaysValidator.setMaxDaysIntoFutureForTest(2025);
    }

    @Test
    public void isValidTest1() {
        localDateTime=LocalDateTime.of(2021, 4, 5, 12, 34);
        boolean res=nowToPlusDaysValidator.isValid(localDateTime,context);
        assertTrue(res);
    }

    @Test
    public void isValidTest2() {
        localDateTime=LocalDateTime.of(2011, 4, 5, 12, 34);
        boolean res=nowToPlusDaysValidator.isValid(localDateTime,context);
        assertFalse(res);
    }
}
