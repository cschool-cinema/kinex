package pl.termosteam.kinex.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinToCurrentYearPlusValidator implements ConstraintValidator<MinToCurrentYearPlus, Short> {

    private short min;
    private short years;

    @Override
    public void initialize(MinToCurrentYearPlus constraint) {
        min = constraint.min();
        years = constraint.addToCurrentYear();
    }

    @Override
    public boolean isValid(Short val, ConstraintValidatorContext context) {
        return val >= min && val <= LocalDate.now().getYear() + years;
    }

    // setter for test
    public void setMinForTest(short min) {
        this.min = min;
    }
    // setter for test
    public void setYearsForTest(short years){
        this.years=years;
    }
}
