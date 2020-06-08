package pl.termosteam.kinex.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MinToCurrentYearPlusValidatorTest {

    private MinToCurrentYearPlusValidator minToCurrentYearPlusValidator;
    private Short input;

    @BeforeEach
    public void init() {
        short min=1800;
        short years=2000;
        minToCurrentYearPlusValidator=new MinToCurrentYearPlusValidator();
        minToCurrentYearPlusValidator = Mockito.spy(minToCurrentYearPlusValidator);
        MinToCurrentYearPlus minToCurrentYearPlus = Mockito.mock(MinToCurrentYearPlus.class);
        Mockito.when(minToCurrentYearPlus.min()).thenReturn(min);
        Mockito.when(minToCurrentYearPlus.addToCurrentYear()).thenReturn(years);
        minToCurrentYearPlusValidator.initialize(minToCurrentYearPlus);
    }

    @Test
    public void isValidTest1() {
        input=new Short("2010");
        boolean res=minToCurrentYearPlusValidator.isValid(input,null);
        assertTrue(res);
    }

    @Test
    public void isValidTest2() {
        input=new Short("1700");
        boolean res=minToCurrentYearPlusValidator.isValid(input,null);
        assertFalse(res);
    }
}