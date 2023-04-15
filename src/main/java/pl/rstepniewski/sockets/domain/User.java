package pl.rstepniewski.sockets.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String username;
    private String password;
    private UserRole role;

    public User(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") UserRole role) {
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

    public UserRole getRole() {
        return role;
    }


}
