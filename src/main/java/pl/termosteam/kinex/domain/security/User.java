package pl.termosteam.kinex.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.termosteam.kinex.validation.UserDataValidationImplementation;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_account")
@Entity(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", allocationSize = 1)
    private int id;

    @Column(nullable = false)
    @Size(max = 512, message = "User name character limit is 128.")
    @NotBlank(message = "User first name cannot be null/blank.")
    private String firstName;

    @Column(nullable = false)
    @Size(max = 512, message = "User name character limit is 128.")
    @NotBlank(message = "User last name name cannot be null/blank.")
    private String lastName;

    @Column(nullable = false)
    @Size(max = 128, message = "User username character limit is 128.")
    @NotBlank(message = "User username cannot be null/blank.")
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    @Size(max = 128, message = "User email character limit is 128.")
    @NotBlank(message = "User email cannot be null/blank.")
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    @Size(max = 512, message = "User hashed password character limit is 512.")
    @NotBlank(message = "User hashed password cannot be null/blank.")
    private String password;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String activationToken;

    @Column(nullable = false)
    private boolean isEnabled;
    @Column(nullable = false)
    private boolean isAuthenticated;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime validAccountTill;
    @Column(nullable = false)
    private LocalDateTime validPasswordTill;
    private LocalDateTime activatedAt;


    public User(String firstName,
                String lastName,
                String username,
                String email,
                String password,
                String salt,
                String role,
                String activationToken,
                boolean isEnabled,
                boolean isAuthenticated,
                LocalDateTime validAccountTill,
                LocalDateTime validPasswordTill) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.role = role;
        this.activationToken = activationToken;
        this.isEnabled = isEnabled;
        this.isAuthenticated = isAuthenticated;
        this.validAccountTill = validAccountTill;
        this.validPasswordTill = validPasswordTill;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] authorities = role.split(",");
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (String string : authorities)
            authorityList.add(new SimpleGrantedAuthority(string));
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return new UserDataValidationImplementation().validateDateExpired(validAccountTill);
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAuthenticated;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return new UserDataValidationImplementation().validateDateExpired(validPasswordTill);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

}
