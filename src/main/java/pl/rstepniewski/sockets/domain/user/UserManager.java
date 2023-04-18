package pl.rstepniewski.sockets.domain.user;

import pl.rstepniewski.sockets.file.FileName;
import pl.rstepniewski.sockets.file.FilePath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserManager {
    Optional<User> getUserByName(String userName) throws IOException;
    List<User> getUserList() throws IOException;

    List<User> getAdminList() throws IOException;
    List<User> getUserAndAdminList() throws IOException;

    boolean addNewUser(User user) throws IOException;

    boolean removeUser(String userName) throws IOException;
}
