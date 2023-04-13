package pl.rstepniewski.sockets.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String username;
    private String password;
    private String role;

    public User(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public User() {
    }
    /*    private Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }*/


    public enum Role {
        USER, ADMIN
    }
}
