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

@Import({UsernameCheckByRegexValidator.class})
public class UsernameConstraintTest extends AbstractConstrainedTest {

    @Test
    public void whenGivenInvalidUsernameShouldReturnViolation() {
        Username username = new Username();
        username.setUsername("");
        Set<ConstraintViolation<Username>> violations = validator.validate(username);
        Assert.assertThat(violations.size(), Matchers.is(1));
    }

    @Test
    public void whenGivenValidUsernameShouldReturnZeroViolations() {
        Username username = new Username();
        username.setUsername("Username");
        Set<ConstraintViolation<Username>> violations = validator.validate(username);
        Assert.assertThat(violations.size(), Matchers.is(0));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    private static class Username {

        @UsernameCheckByRegex
        private String username;
    }
}
