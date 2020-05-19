package pl.termosteam.kinex.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class NowToPlusDaysValidator implements ConstraintValidator<NowToPlusDays, LocalDateTime> {

    private int maxDaysIntoFuture;

    @Override
    public void initialize(NowToPlusDays constraint) {
        maxDaysIntoFuture = constraint.days();
    }

    @Override
    public boolean isValid(LocalDateTime dt, ConstraintValidatorContext context) {
        LocalDateTime now = LocalDateTime.now();

        return dt.isAfter(now) && dt.isBefore(now.plusDays(maxDaysIntoFuture));
    }
    //setter for test
    public void setMaxDaysIntoFutureForTest(int maxDaysIntoFuture){
        this.maxDaysIntoFuture=maxDaysIntoFuture;
    }
}
