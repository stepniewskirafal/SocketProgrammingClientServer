package pl.rstepniewski.sockets.domain;

public enum UserRole {
    USER("USER"), ADMIN("ADMIN");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
