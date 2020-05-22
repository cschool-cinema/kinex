package pl.termosteam.kinex.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringCheckByRegexValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringCheckByRegex {
    String message() default "Invalid username according to the provided regex pattern.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String patternRegex();
}
