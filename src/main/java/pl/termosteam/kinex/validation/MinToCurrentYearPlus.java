package pl.termosteam.kinex.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinToCurrentYearPlusValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinToCurrentYearPlus {
    String message() default "Invalid year.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    short min();

    short addToCurrentYear();
}