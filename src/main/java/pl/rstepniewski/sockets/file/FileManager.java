package pl.rstepniewski.sockets.file;

import pl.rstepniewski.sockets.domain.user.User;

import java.io.IOException;
import java.util.List;

public interface FileManager {
    List<User> importAllUsersFromFiles(String filePath) throws IOException;
    void exportUserData(List<User> userList, FilePath filePath, FileName fileName) throws IOException;

}
