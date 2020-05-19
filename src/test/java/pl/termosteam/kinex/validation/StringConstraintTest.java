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

@Import({StringCheckByRegexValidator.class})
public class StringConstraintTest extends AbstractConstrainedTest {

    @Test
    public void whenGivenInvalidStringShouldReturnViolation() {
        SimpleString simpleString = new SimpleString();
        simpleString.setString("<script></script>");
        Set<ConstraintViolation<SimpleString>> violations = validator.validate(simpleString);
        Assert.assertThat(violations.size(), Matchers.is(1));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public class SimpleString {

        @StringCheckByRegex(patternRegex = "[A-Z][a-z]*")
        private String string;
    }
}
