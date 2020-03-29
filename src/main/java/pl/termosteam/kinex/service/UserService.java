package pl.termosteam.kinex.service;

import lombok.AllArgsConstructor;
import org.apache.commons.codec.digest.Crypt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.termosteam.kinex.configuration.jwt.JwtToken;
import pl.termosteam.kinex.configuration.properties.DeveloperConfiguration;
import pl.termosteam.kinex.domain.Role;
import pl.termosteam.kinex.domain.User;
import pl.termosteam.kinex.dto.UserRequestDto;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service(value = "userService")
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final JwtToken jwtTokenUtil;
    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;
    private final DeveloperConfiguration developerConfiguration;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return loadUserByUsernameOrEmail(usernameOrEmail);
    }

    public User loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
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
        final String token = jwtTokenUtil.generateActivationToken(user.getActivationUUID());

        user.setInMemoryActivationToken(token);

        if (developerConfiguration.getIsReturnActivationToken()) {
            sendEmailService.sendMail(userRequestDTO.getEmail(), "activation token for kinex api", token);
        }

        userRepository.save(user);

        return Optional.of(user);
    }

    private void validateIfEmailAndUsernameExists(UserRequestDto userRequestDTO) {
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
    public Optional<User> activateByToken(String usernameOrEmail, String token) {
        User user = loadUserByUsernameOrEmail(usernameOrEmail);

        if (!jwtTokenUtil.validateActivationToken(token, user.getActivationUUID())) {
            throw new ValidationException("Activation token is not valid or expired.");
        }

        user.setEnabled(true);
        user.setActivatedAt(LocalDateTime.now());
        return Optional.of(user);
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

    public UserDetails getUserDetailNotNullIfAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        return null;
    }

    public User getUserNotNullIfAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return loadUserByUsernameOrEmail(((UserDetails) principal).getUsername());
        }
        return null;
    }

}
