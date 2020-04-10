package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.Crypt;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.termosteam.kinex.configuration.jwt.JwtToken;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.dto.UserResponseDto;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service(value = "userService")
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final JwtToken jwtTokenUtil;
    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.trace("UserService->loadUserByUsername: usernameOrEmail" + usernameOrEmail);
        return loadUserByUsernameOrEmail(usernameOrEmail);
    }

    public User loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
        logger.trace("UserService->loadUserByUsernameOrEmail: usernameOrEmail" + usernameOrEmail);
        User user;
        if (usernameOrEmail.contains("@")) {
            user = userRepository.findByEmail(usernameOrEmail);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with email: " + usernameOrEmail);
            }
        } else {
            user = userRepository.findByUsername(usernameOrEmail);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + usernameOrEmail);
            }
        }
        if (!user.isAccountNonExpired() && !user.isCredentialsNonExpired()) {
            throw new ValidationException("User: " + user.getUsername()
                    + " has account validation time for " + user.getValidAccountTill() +
                    " and credentials validation time for " + user.getValidPasswordTill());
        }
        logger.trace("UserService->loadUserByUsernameOrEmail: found user:\n" + user);
        return user;
    }

    @Transactional
    public Optional<User> addUserWithRole(Role ROLE, UserRequestDto userRequestDTO) {
        validateIfEmailAndUsernameExists(userRequestDTO);
        String UUID = ActivateService.generateUUID();
        String salt = UUID.replace("-", "");
        User user = new User(
                userRequestDTO.getFirstName(),
                userRequestDTO.getLastName(),
                userRequestDTO.getUsername(),
                userRequestDTO.getEmail(),
                Crypt.crypt(userRequestDTO.getPassword(), salt),
                salt,
                ROLE.toString(),
                UUID,
                false,
                false,
                LocalDateTime.now().plusYears(1),
                LocalDateTime.now().plusYears(1));
        final String token = jwtTokenUtil.generateActivationToken(
                user.getActivationUUID(),
                new Date(System.currentTimeMillis()));
        user.setInMemoryActivationToken(token);
        userRepository.save(user);
        logger.info("Added user " + userRequestDTO.getUsername() + " with role " + ROLE);
        return Optional.of(user);
    }

    private void validateIfEmailAndUsernameExists(UserRequestDto userRequestDTO) {
        logger.trace("UserService->validateIfEmailAndUsernameExists: userRequestDTO" + userRequestDTO);
        if (!ifUserAlreadyExistsAndDeleted(userRequestDTO.getUsername())) {
            if (ifEmailAlreadyExists(userRequestDTO.getEmail())) {
                throw new ValidationException("Email \"" + userRequestDTO.getEmail() +
                        "\" already registered. Please authenticate.");
            }

            if (ifUsernameAlreadyExists(userRequestDTO.getUsername())) {
                throw new ValidationException("Username \"" + userRequestDTO.getUsername() +
                        "\" already registered. Please authenticate.");
            }
        }
    }

    @Transactional
    public void activateByToken(String usernameOrEmail, String token) {
        logger.trace("UserService->activateByToken: activate user: " + usernameOrEmail + " with token " + token);
        User user = loadUserByUsernameOrEmail(usernameOrEmail);
        if (!jwtTokenUtil.validateActivationToken(token, user.getActivationUUID())) {
            final String newToken = jwtTokenUtil.generateActivationToken(
                    user.getActivationUUID(),
                    new Date(System.currentTimeMillis()));
            user.setInMemoryActivationToken(newToken);
            sendEmailService.sendMail(user.getEmail(), "new activation token for kinex api", token);
            userRepository.save(user);
            throw new ValidationException("Activation token is not valid or expired. Token resend on email.");
        }
        user.setEnabled(true);
        user.setActivatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public boolean ifOwnerAlreadyExists() {
        return userRepository.existsByRole(Role.OWNER.toString());
    }

    public boolean ifUserAlreadyExistsAndDeleted(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.isDeleted();
        } else {
            return false;
        }
    }

    public boolean ifUsernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean ifEmailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserNotNullIfAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {

            return loadUserByUsernameOrEmail(((UserDetails) principal).getUsername());
        }
        return null;
    }

    public void activateGuest(User guest) {
        guest.setEnabled(true);
        guest.setActivatedAt(LocalDateTime.now());
        userRepository.save(guest);
    }

    public UserResponseDto getUser(String usernameOrEmail) {

        User userAuthenticated = getUserNotNullIfAuthenticated();
        User userFromSearch = getUserByUsernameOrEmail(usernameOrEmail);

        if (userAuthenticated.getRole().equals(Role.USER.getRole())) {
            if (userAuthenticated.equals(userFromSearch)) {
                return getUserResponseDto(userFromSearch);
            }
        }

        if (userAuthenticated.getRole().equals(Role.MANAGER.getRole())) {
            if (userAuthenticated.equals(userFromSearch) ||
                    userFromSearch.getRole().equals(Role.USER.getRole())) {
                return getUserResponseDto(userFromSearch);
            }
        }

        if (userAuthenticated.getRole().equals(Role.ADMINISTRATOR.getRole())) {
            if (userAuthenticated.equals(userFromSearch) ||
                    userFromSearch.getRole().equals(Role.MANAGER.getRole()) ||
                    userFromSearch.getRole().equals(Role.USER.getRole())) {
                return getUserResponseDto(userFromSearch);
            }
        }

        if (userAuthenticated.getRole().equals(Role.OWNER.getRole())) {
            if (userAuthenticated.equals(userFromSearch) ||
                    userFromSearch.getRole().equals(Role.ADMINISTRATOR.getRole()) ||
                    userFromSearch.getRole().equals(Role.MANAGER.getRole()) ||
                    userFromSearch.getRole().equals(Role.USER.getRole())) {
                return getUserResponseDto(userFromSearch);
            }
        }

        throw new ValidationException("Authenticated user " + userAuthenticated.getUsername()
                + " with " + userAuthenticated.getRole()
                + "don't have access to the account: " + usernameOrEmail);

    }

    UserResponseDto getUserResponseDto(User userFromSearch) {
        return new UserResponseDto(userFromSearch.getFirstName(),
                userFromSearch.getLastName(),
                userFromSearch.getUsername(),
                userFromSearch.getEmail());
    }

    User getUserByUsernameOrEmail(String usernameOrEmail) {
        User userFromSearch;
        if (usernameOrEmail.contains("@")) {
            userFromSearch = userRepository.findByEmail(usernameOrEmail);
        } else {
            userFromSearch = userRepository.findByUsername(usernameOrEmail);
        }
        if (userFromSearch == null) {
            throw new ObjectNotFoundException("There is no user with such email or username", usernameOrEmail);
        }
        return userFromSearch;

    }

    public boolean ifUserAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
