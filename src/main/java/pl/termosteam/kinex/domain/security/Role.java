package pl.termosteam.kinex.domain.security;

public enum Role {

    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    ADMINISTRATOR("ROLE_ADMINISTRATOR"),
    OWNER("ROLE_OWNER");

    private final String role;

    Role(final String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

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
