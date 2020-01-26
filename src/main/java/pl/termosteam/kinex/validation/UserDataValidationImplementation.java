package pl.termosteam.kinex.validation;

import org.passay.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.termosteam.kinex.domain.security.UserDTO;
import pl.termosteam.kinex.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserDataValidationImplementation implements UserDataValidation {
    //TODO: moove all static strings to the configurations files
    //TODO: add more sophisticated regex pattern for first and last names
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\" +
            ".[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String USERNAME_PATTERN = "\\b[a-zA-Z][a-zA-Z0-9\\-._]{3,}\\b"; //  the starting letter of
    // the username must be a character, Length >=3, Valid characters: a-z, A-Z, 0-9, points, dashes and underscores.
    private static final String FIRST_NAME_PATTERN = "[A-Z][a-z]*";
    private static final String LAST_NAME_PATTERN = "[A-Z]+([ '-][a-zA-Z]+)*";
    Logger logger = LoggerFactory.getLogger(UserDataValidation.class);
    private Pattern pattern;
    private Matcher matcher;
    private String errorMessage;

    @Override
    public boolean validateDateExpired(LocalDateTime tillDateTime) {
        return LocalDateTime.now().isBefore(tillDateTime);
    }

    @Override
    public void userDataValidation(UserDTO userDTO) {
        if (!validateData(userDTO.getUsername(), USERNAME_PATTERN)) {
            throw new ValidationException("Email validation failed, please provide proper email");
        }
        if (!validateData(userDTO.getFirstName(), FIRST_NAME_PATTERN)) {
            throw new ValidationException("First Name validation failed, please provide proper first name:" +
                    " starting from Upper Character and rest is only characters");
        }
        if (!validateData(userDTO.getLastName(), LAST_NAME_PATTERN)) {
            throw new ValidationException("Last Name validation failed, please provide proper last name:" +
                    "starting from Upper Character and rest is only characters allowed");
        }
        if (!validateData(userDTO.getEmail(), EMAIL_PATTERN)) {
            throw new ValidationException("Email validation failed, please provide proper email");
        }
        if (!isValidPassword(userDTO.getPassword())) {
            logger.error(this.errorMessage);
            throw new ValidationException(
                    "Password validation failed, please provide proper password: \n" + errorMessage);
        }
    }


    private boolean validateData(final String data, final String PATTERN) {
        pattern = Pattern.compile(PATTERN);
        matcher = pattern.matcher(data);
        return matcher.matches();
    }

    //TODO: password validation done for org.passay in version 1.0, for version 1.5 not working, Validation rules
    // should be changed
    public boolean isValidPassword(final String password) {
        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new NumericalSequenceRule(3, false),
                new AlphabeticalSequenceRule(3, false),
                new QwertySequenceRule(3, false),
                new WhitespaceRule()));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        this.errorMessage = validator.getMessages(result).toArray().toString();
        return false;
    }
}
