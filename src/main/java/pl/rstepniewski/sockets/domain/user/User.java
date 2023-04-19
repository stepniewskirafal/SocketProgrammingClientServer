package pl.rstepniewski.sockets.domain.user;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private String username;
    private String password;
    private UserRole role;

    public User(final  @JsonProperty("username") String username, final @JsonProperty("password") String password, final @JsonProperty("role") UserRole role) {
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
