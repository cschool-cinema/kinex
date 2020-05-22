package pl.termosteam.kinex.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPassword {

    String message() default "Invalid password at least.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
