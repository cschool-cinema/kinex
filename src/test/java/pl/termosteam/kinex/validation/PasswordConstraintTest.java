package pl.termosteam.kinex.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Import({CustomPasswordValidator.class})
public class PasswordConstraintTest extends AbstractConstrainedTest {

    @Test
    public void whenGivenInvalidPasswordShouldReturnViolation() {
        Password pwd = new Password();
        pwd.setPassword("Uttttk,lk,");
        Set<ConstraintViolation<Password>> violations = validator.validate(pwd);
        Assert.assertThat(violations.size(), Matchers.is(1));
    }

    @Test
    public void whenGivenValidPasswordShouldReturnZeroViolation() {
        Password pwd = new Password();
        pwd.setPassword("Ur1,");
        Set<ConstraintViolation<Password>> violations = validator.validate(pwd);
        Assert.assertThat(violations.size(), Matchers.is(0));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    private static class Password {

        @CustomPassword
        private String password;
    }
}
