package pl.rstepniewski.sockets.domain.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.rstepniewski.sockets.domain.user.User;
import pl.rstepniewski.sockets.domain.user.UserRole;
import pl.rstepniewski.sockets.file.FileName;
import pl.rstepniewski.sockets.file.FilePath;
import pl.rstepniewski.sockets.file.FileService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MessageService {
    ObjectMapper objectMapper = new ObjectMapper();

    FileService fileService = new FileService();

    public Optional<List<Message>> getUserMessages(User user) throws IOException {
        Optional<List<Message>> messageList = Optional.empty();
        switch (user.getRole()) {
            case USER -> {
                String filePath = FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/" + user.getUsername() + "/" + FileName.MESSAGE_FILENAME.getFileName();
                File file = new File(filePath);
                if(file.exists()){
                    messageList = Optional.of(fileService.importMessagesFromJsonFiles(
                            FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/" + user.getUsername() + "/" + FileName.MESSAGE_FILENAME.getFileName()));
                    fileService.deleteJsonMessagesFiles(FilePath.USER_MESSAGE_FOLDER, user.getUsername());
                }
            }
            case ADMIN -> {
                String filePath = FilePath.ADMIN_MESSAGE_FOLDER.getFolderPath() + "/" + user.getUsername() + "/" + FileName.MESSAGE_FILENAME.getFileName();
                File file = new File(filePath);
                if(file.exists()) {
                    messageList =  Optional.of(fileService.importMessagesFromJsonFiles(
                            FilePath.ADMIN_MESSAGE_FOLDER.getFolderPath() + "/" + user.getUsername() + "/" + FileName.MESSAGE_FILENAME.getFileName()));
                    fileService.deleteJsonMessagesFiles(FilePath.ADMIN_MESSAGE_FOLDER, user.getUsername());
                }
            }
        }
        return messageList;
    }

    public void sendMessage(UserRole role, String recipient, List<Message> messageList ) throws IOException {
        switch (role) {
            case USER -> {
                fileService.exportMessagesFromJsonFiles(messageList, FilePath.USER_MESSAGE_FOLDER, recipient  ,FileName.MESSAGE_FILENAME);
            }
            case ADMIN -> {
                fileService.exportMessagesFromJsonFiles(messageList, FilePath.ADMIN_MESSAGE_FOLDER, recipient ,FileName.MESSAGE_FILENAME);
            }
        }
    }
}
