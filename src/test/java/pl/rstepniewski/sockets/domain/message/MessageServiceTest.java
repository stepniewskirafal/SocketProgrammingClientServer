package pl.rstepniewski.sockets.domain.message;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.rstepniewski.sockets.domain.user.User;
import pl.rstepniewski.sockets.domain.user.UserRole;
import pl.rstepniewski.sockets.io.file.FilePath;
import pl.rstepniewski.sockets.io.file.FileService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {
    private FileService fileService;
    private ObjectWriter writer;
    ObjectMapper objectMapper;
    private MessageService messageService;
    private List<Message> messageList;

    @BeforeEach
    void init(){
        messageService = new MessageService();
        fileService = new FileService();
        objectMapper = new ObjectMapper();
        writer = objectMapper.writer(new DefaultPrettyPrinter());

        final Message message = new Message("test", "test", "user1", "user1");
        messageList = new ArrayList<>();
        messageList.add(message);
    }

    @Test
    @DisplayName("Get user messages")
    void getUserMessages() throws IOException {
        final String userMessageFilePath = "src/main/resources/json/message/user/user1/message.json";
        File jsonUsersFile = new File(userMessageFilePath);
        jsonUsersFile.delete();
        writer.writeValue(jsonUsersFile, messageList);

        Optional<List<Message>> userMessages = messageService.getUserMessages(new User("user1", "user1", UserRole.USER));

        Assertions.assertFalse(userMessages.isEmpty());
    }

    @Test
    void sendMessage() throws IOException {
        final String newDirPath = "src/main/resources/json/message/user/user3";
        File newDir = new File(newDirPath);
        newDir.mkdir();

        final String newMessagePath = newDirPath+"/message.json";
        File newMessageFile = new File(newMessagePath);

        messageService.sendMessage(UserRole.USER, "user3", messageList);

        Assertions.assertTrue(newMessageFile.exists());
    }
}