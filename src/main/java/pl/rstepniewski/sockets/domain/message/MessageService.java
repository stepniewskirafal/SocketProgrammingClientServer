package pl.rstepniewski.sockets.domain.message;

/**
 * Created by rafal on 19.04.2023
 *
 * @author : rafal
 * @date : 19.04.2023
 * @project : SocketProgrammingClientServer
 */

import pl.rstepniewski.sockets.domain.user.User;
import pl.rstepniewski.sockets.domain.user.UserRole;
import pl.rstepniewski.sockets.io.file.FileName;
import pl.rstepniewski.sockets.io.file.FilePath;
import pl.rstepniewski.sockets.io.file.FileService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MessageService {
    FileService fileService = new FileService();

    public Optional<List<Message>> getUserMessages(final User user) throws IOException {
        Optional<List<Message>> messageList = Optional.empty();
        final UserRole loggedUserRole = user.getRole();
        final String filePath;
        final File file;

        if (loggedUserRole == UserRole.USER){
            filePath = FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/" + user.getUsername() + "/" + FileName.MESSAGE_FILENAME.getFileName();
            file = new File(filePath);
            if(file.exists()){
                messageList = Optional.of(fileService.importDataFromJsonFiles( filePath, Message[].class) );
                fileService.deleteJsonMessagesFiles(FilePath.USER_MESSAGE_FOLDER, user.getUsername());
            }
        } else if (loggedUserRole == UserRole.ADMIN) {
            filePath = FilePath.ADMIN_MESSAGE_FOLDER.getFolderPath() + "/" + user.getUsername() + "/" + FileName.MESSAGE_FILENAME.getFileName();
            file = new File(filePath);
            if(file.exists()) {
                messageList =  Optional.of(fileService.importDataFromJsonFiles( filePath, Message[].class)
                );
                fileService.deleteJsonMessagesFiles(FilePath.ADMIN_MESSAGE_FOLDER, user.getUsername());
            }
        }

        return messageList;
    }

    public boolean sendMessage(final UserRole role, final String recipient, final List<Message> messageList ) throws IOException {
        boolean isMessageSent = true;
        final String filePath;
        final File file;
        final List<Message> allMessagesToSave;
        final Optional<List<Message>> userMessageBox;

        if (role == UserRole.USER){
            filePath = FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/" + recipient + "/" + FileName.MESSAGE_FILENAME.getFileName();
            file = new File(filePath);
            if(file.exists()) {
                userMessageBox = Optional.of(fileService.importDataFromJsonFiles( filePath, Message[].class));
                if(userMessageBox.isEmpty()){
                    fileService.exportDataToJsonFiles(messageList, FilePath.USER_MESSAGE_FOLDER, recipient, FileName.MESSAGE_FILENAME);
                } else if (userMessageBox.get().size() < MessageConst.MAX_NUMBER_OF_MESSAGES.getMessageLenght()) {
                    allMessagesToSave = userMessageBox.get();
                    allMessagesToSave.addAll(messageList);
                    fileService.exportDataToJsonFiles(allMessagesToSave, FilePath.USER_MESSAGE_FOLDER, recipient, FileName.MESSAGE_FILENAME);
                }else {
                    isMessageSent = false;
                }
            }else {
                fileService.exportDataToJsonFiles(messageList, FilePath.USER_MESSAGE_FOLDER, recipient, FileName.MESSAGE_FILENAME);
            }
        } else if (role == UserRole.ADMIN) {
            filePath = FilePath.ADMIN_MESSAGE_FOLDER.getFolderPath() + "/" + recipient + "/" + FileName.MESSAGE_FILENAME.getFileName();
            file = new File(filePath);
            if(file.exists()) {
                userMessageBox = Optional.of(fileService.importDataFromJsonFiles( filePath, Message[].class) );
                allMessagesToSave = userMessageBox.get();
                allMessagesToSave.addAll(messageList);
                fileService.exportDataToJsonFiles(allMessagesToSave, FilePath.ADMIN_MESSAGE_FOLDER, recipient, FileName.MESSAGE_FILENAME);
            }else {
                fileService.exportDataToJsonFiles(messageList, FilePath.ADMIN_MESSAGE_FOLDER, recipient, FileName.MESSAGE_FILENAME);
            }
        }

        return isMessageSent;
    }
}
