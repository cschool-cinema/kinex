package pl.termosteam.kinex.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", 0),
    USER("ROLE_USER", 1),
    MANAGER("ROLE_MANAGER", 2),
    ADMINISTRATOR("ROLE_ADMINISTRATOR", 3),
    OWNER("ROLE_OWNER", 4);

    private final String role;
    private final int hierarchy;

    @Override
    public String toString() {
        return this.getRole();
    }

    public static String getRoleHierarchy() {
        return Role.OWNER + " > " +
                Role.ADMINISTRATOR + " > " +
                Role.MANAGER + " > " +
                Role.USER + " > " +
                Role.GUEST;
    }
}
