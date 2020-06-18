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

@Import({NameCheckByRegexValidator.class})
public class NameConstraintTest extends AbstractConstrainedTest {

    @Test
    public void whenGivenInvalidNameShouldReturnViolation() {
        Name pwd = new Name();
        pwd.setName("@1235sdfds");
        Set<ConstraintViolation<Name>> violations = validator.validate(pwd);
        Assert.assertThat(violations.size(), Matchers.is(1));
        Assert.assertThat(violations.iterator().next().getMessage(),
                Matchers.is("Invalid username according to the provided regex pattern."));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    private static class Name {

        @NameCheckByRegex
        private String name;
    }
}
