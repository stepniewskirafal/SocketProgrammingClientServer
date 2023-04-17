package pl.rstepniewski.sockets.file;

import pl.rstepniewski.sockets.domain.message.Message;
import pl.rstepniewski.sockets.domain.user.User;

import java.io.IOException;
import java.util.List;

public interface FileManager {
    List<User> importUsersFromJsonFiles(String filePath) throws IOException;
    void exportUsersToJsonFiles(List<User> userList, FilePath filePath, FileName fileName) throws IOException;
    List<Message> importMessagesFromJsonFiles(String filePath) throws IOException;
    void exportMessagesFromJsonFiles(List<Message> messageList, FilePath filePath, String recipient, FileName fileName) throws IOException;
    void deleteJsonMessagesFiles(FilePath filePath, String recipient) throws IOException;

}
