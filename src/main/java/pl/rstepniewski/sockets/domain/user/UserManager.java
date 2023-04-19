package pl.rstepniewski.sockets.domain.user;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

import java.io.IOException;
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
