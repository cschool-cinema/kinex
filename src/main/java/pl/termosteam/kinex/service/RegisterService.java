package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.termosteam.kinex.configuration.properties.ApplicationProperties;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.exception.ValidationException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterService {

    private final UserService userService;
    private final ApplicationProperties applicationProperties;
    private final SendEmailService sendEmailService;
    private final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    public String registerUserWithRole(Role ROLE, UserRequestDto userRequestDto) {
        logger.trace("RegisterService->registerUserWithRole: ROLE: " + ROLE + "\n       userRequestDto input: " + userRequestDto);
        if (ROLE.equals(Role.OWNER) && !ownerIsAuthorised() && checkOwnerAlreadyRegistered()) {
            throw new ValidationException("Owner is already registered, if you want to register next owner, please login as owner.");
        }

        if (!checkOwnerAlreadyRegistered() && !ROLE.equals(Role.OWNER)) {
            throw new ValidationException(
                    "Owner is not registered, if you want to register user with " + ROLE + " first register OWNER role");
        }

        Optional<User> user = addUserRoleSelection(ROLE, userRequestDto);
        if (!user.isPresent()) {
            throw new ValidationException("null user RegisterService->registerUserWithRole->line \"if (developerConfiguration.getIsReturnActivationToken())...\"");
        }

        if (applicationProperties.getDeveloperConfiguration().getIsReturnActivationToken()) {
            logger.debug("RegisterService->registerUserWithRole: developer mode - automatic activation");

            userService.activateByToken(user.get().getUsername(), user.get().getInMemoryActivationToken());
            return "DEVELOPER MODE: User is registered and activated automatically.";
        } else {
            sendEmailService.sendMail(userRequestDto.getEmail(), "activation token for kinex api", user.get().getInMemoryActivationToken());
            logger.info("Sending email (" + userRequestDto.getEmail() + ") to account with activation token.");
            return "You account with the username \"" + user.get().getUsername() +
                    "\" has been registered, please activate account using the token sent to the provided email: " +
                    user.get().getEmail();
        }
    }

    private Optional<User> addUserRoleSelection(Role ROLE, UserRequestDto userRequestDto) {
        logger.trace("RegisterService->addUserRoleSelection: adding user with role: " + ROLE + ROLE + "\n       userRequestDTO input: " + userRequestDto);
        switch (ROLE) {
            case OWNER:
                return userService.addUserWithRole(Role.OWNER, userRequestDto);
            case ADMINISTRATOR:
                return userService.addUserWithRole(Role.ADMINISTRATOR, userRequestDto);
            case MANAGER:
                return userService.addUserWithRole(Role.MANAGER, userRequestDto);
            case USER:
                return userService.addUserWithRole(Role.USER, userRequestDto);
            case GUEST:
                return userService.addUserWithRole(Role.GUEST, userRequestDto);
            default:
                return Optional.empty();
        }

    }

    public boolean checkOwnerAlreadyRegistered() {
        return userService.ifOwnerAlreadyExists();
    }

    private boolean ownerIsAuthorised() {
        logger.trace("RegisterService->ownerIsAuthorised: checking if the owner is authorised");
        User user = userService.getUserNotNullIfAuthenticated();
        logger.trace("RegisterService->ownerIsAuthorised: authorised user: " + user);
        return user != null && user.getRole().equals(Role.OWNER.toString());
    }
}
