package pl.rstepniewski.sockets.file;

import pl.rstepniewski.sockets.domain.User;

import java.io.IOException;
import java.util.List;

public interface FileManager {
    List<User> importAllUsersFromFiles(String filePath) throws IOException;
    void exportUserData(List<User> userList, String filePath, String jsonFilename) throws IOException;

}
