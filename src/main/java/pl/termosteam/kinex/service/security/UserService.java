package pl.termosteam.kinex.service.security;

import pl.termosteam.kinex.domain.security.Role;
import pl.termosteam.kinex.domain.security.User;
import pl.termosteam.kinex.domain.security.UserDTO;

import java.util.Optional;

public interface UserService {

    /**
     * Add a user with specific role.
     *
     * @param ROLE    enum type of the current user ()
     * @param userDTO UserDTO object
     * @return Optional<User>
     */
    Optional<User> addUserWithRole(Role ROLE, UserDTO userDTO);

    /**
     * Finds a user by its username or email.
     *
     * @param usernameOrEmail
     * @return User
     */
    User loadUserByUsernameOrEmail(String usernameOrEmail);

    /**
     * Finds a user by its activation token.
     *
     * @param usernameOrEmail user username or email
     * @param token           user activation token
     * @return
     */
    Optional<User> activateByToken(String usernameOrEmail, String token);

    /**
     * Logs out the given input {@code user}.
     *
     * @param user the user to logout
     */
    void logout(User user);

    /**
     * Verify if user with role OWNER exists in the DataBase
     *
     * @return true if owner exists and false if there is no owner and there is no other users
     */
    boolean ifOwnerAlreadyExists();
}
