package pl.rstepniewski.sockets.io.file;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pl.rstepniewski.sockets.domain.message.Message;
import pl.rstepniewski.sockets.domain.user.User;
import pl.rstepniewski.sockets.domain.user.UserRole;
import pl.rstepniewski.sockets.domain.user.UserService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private FileService fileService;
    private ObjectWriter writer;
    ObjectMapper objectMapper;
    String userDataFilePath;
    String userMessageFilePath;
    File jsonUsersFile;
    @BeforeEach
    void init() throws IOException {
        fileService = new FileService();
        objectMapper = new ObjectMapper();
        writer = objectMapper.writer(new DefaultPrettyPrinter());
    }

    @DisplayName("Imported user is user1")
    @Test
    void importDataFromJsonFiles() throws IOException {
        final User usertest1 = new User("user1", "user1", UserRole.USER);
        List<User> userList = new ArrayList<>();
        userList.add(usertest1);

        userDataFilePath = "src/main/resources/json/users/user/user.json";
        jsonUsersFile = new File(userDataFilePath);
        jsonUsersFile.delete();
        writer.writeValue(jsonUsersFile, userList);

        List<User> userListActual = fileService.importDataFromJsonFiles(userDataFilePath, User[].class);
        List<User> userListExpected = List.of(new User("user1", "user1", UserRole.USER));
        Assertions.assertEquals(userListActual.toArray()[0], userListExpected.toArray()[0]);
    }

    @DisplayName("Exported user is user1 using 4Agruments")
    @Test
    void exportDataToJsonFiles4Agruments() throws IOException {
        List<User> userListExpected = List.of(new User("user1", "user1", UserRole.USER));

        fileService.exportDataToJsonFiles(userListExpected, FilePath.USER_MESSAGE_FOLDER, "user1", FileName.MESSAGE_FILENAME);

        File file = new File(FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/user1/" + FileName.MESSAGE_FILENAME.getFileName());
        Assertions.assertTrue(file.exists());
    }

    @DisplayName("Exported user is user1 using 3Agruments")
    @Test
    void exportDataToJsonFiles3Agruments() throws IOException {
        List<User> userListExpected = List.of(new User("user1", "user1", UserRole.USER));

        fileService.exportDataToJsonFiles(userListExpected, FilePath.USER_MESSAGE_FOLDER, FileName.MESSAGE_FILENAME);

        File file = new File(FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/" + FileName.MESSAGE_FILENAME.getFileName());
        Assertions.assertTrue(file.exists());
    }

    @DisplayName("Delete Json File")
    @Test
    void deleteJsonMessagesFiles() throws IOException {
        final User usertest1 = new User("user1", "user1", UserRole.USER);
        List<User> userListExpected = new ArrayList<>();
        userListExpected.add(usertest1);

        userMessageFilePath = "src/main/resources/json/message/user/user1/message.json";
        jsonUsersFile = new File(userMessageFilePath);
        jsonUsersFile.delete();
        writer.writeValue(jsonUsersFile, userListExpected);

        fileService.deleteJsonMessagesFiles(FilePath.USER_MESSAGE_FOLDER, "user1");

        File file = new File(FilePath.USER_MESSAGE_FOLDER.getFolderPath() + "/" + "user1/message.json");
        Assertions.assertFalse(file.exists());
    }
}