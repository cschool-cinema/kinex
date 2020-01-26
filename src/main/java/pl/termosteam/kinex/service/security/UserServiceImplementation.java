package pl.termosteam.kinex.service.security;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.termosteam.kinex.configuration.JwtToken;
import pl.termosteam.kinex.domain.security.Role;
import pl.termosteam.kinex.domain.security.User;
import pl.termosteam.kinex.domain.security.UserDTO;
import pl.termosteam.kinex.exception.ValidationException;
import pl.termosteam.kinex.repository.security.UserRepository;
import pl.termosteam.kinex.service.SendEmailService;
import pl.termosteam.kinex.validation.UserDataValidation;

import java.time.LocalDateTime;
import java.util.Optional;

@Service(value = "userService")
public class UserServiceImplementation implements UserService, UserDetailsService {

    private final JwtToken jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserDataValidation userDataValidation;
    private final SendEmailService sendEmailService;

    @Autowired
    public UserServiceImplementation(JwtToken jwtTokenUtil,
                                     UserRepository userRepository,
                                     UserDataValidation userDataValidation,
                                     SendEmailService sendEmailService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.userDataValidation = userDataValidation;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return loadUserByUsernameOrEmail(usernameOrEmail);
    }

    @Override
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

    @Override
    @Transactional
    public Optional<User> addUserWithRole(Role ROLE, UserDTO userDTO) {
        userDataValidation.userDataValidation(userDTO);
        String UUID = ActivationService.generateUUID();
        String salt = UUID.replace("-", "");
        User user = new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                Crypt.crypt(userDTO.getPassword(), salt),
                salt,
                ROLE.toString(),
                UUID,
                false,
                false,
                LocalDateTime.now().plusYears(1),
                LocalDateTime.now().plusYears(1));
        final String token = jwtTokenUtil.generateActivationToken(user.getActivationToken());

        sendEmailService.sendMail(userDTO.getEmail(), "activation token for kinex api", token);

        userRepository.save(user);

        return Optional.of(user);
    }

    @Override
    @Transactional
    public Optional<User> activateByToken(String usernameOrEmail, String token) {
        User user = loadUserByUsernameOrEmail(usernameOrEmail);

        if (!jwtTokenUtil.validateActivationToken(token, user.getActivationToken())) {
            throw new ValidationException("Activation token is not valid or expired.");
        }

        user.setEnabled(true);
        user.setActivatedAt(LocalDateTime.now());
        return Optional.of(user);
    }

    @Override
    public void logout(User user) {

    }

    @Override
    public boolean ifOwnerAlreadyExists() {
        return userRepository.existsByRole(Role.OWNER.toString());
    }

}
