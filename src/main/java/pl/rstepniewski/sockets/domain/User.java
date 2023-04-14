package pl.rstepniewski.sockets.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String username;
    private String password;
    private Role role;

    public User(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public enum Role {
        USER, ADMIN
    }
}
