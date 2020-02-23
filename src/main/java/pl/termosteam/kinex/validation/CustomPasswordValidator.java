package pl.termosteam.kinex.validation;

import org.passay.*;
import pl.termosteam.kinex.configuration.PasswordPatternConfiguration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CustomPasswordValidator implements ConstraintValidator<CustomPassword, String> {

   private final PasswordPatternConfiguration configuration;

   public CustomPasswordValidator(PasswordPatternConfiguration passwordPatternConfiguration) {
      this.configuration = passwordPatternConfiguration;
   }

   public void initialize(CustomPassword constraint) {
   }

   public boolean isValid(String password, ConstraintValidatorContext context) {
      final PasswordValidator validator = new PasswordValidator(Arrays.asList(
              new LengthRule(configuration.getLengthMinRule(), configuration.getLengthMaxRule()),
              new UppercaseCharacterRule(configuration.getUppercaseCharacterRule()),
              new DigitCharacterRule(configuration.getDigitCharacterRule()),
              new SpecialCharacterRule(configuration.getSpecialCharacterRule()),
              new NumericalSequenceRule(configuration.getNumericalSequenceRule(), false),
              new AlphabeticalSequenceRule(configuration.getAlphabeticalSequenceRule(), false),
              new QwertySequenceRule(configuration.getQwertySequenceRule(), false),
              new WhitespaceRule()));
      final RuleResult result = validator.validate(new PasswordData(password));
      return result.isValid();
      //this.errorMessage = validator.getMessages(result).toArray().toString();
   }
}