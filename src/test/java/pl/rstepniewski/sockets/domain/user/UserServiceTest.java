package pl.rstepniewski.sockets.domain.user;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.*;
import pl.rstepniewski.sockets.io.file.FileService;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
class UserServiceTest {
    private FileService fileService;
    private UserService userService;
    private ObjectWriter writer;
    ObjectMapper objectMapper;
    String userDataFilePath;
    String adminDataFilePath;
    File jsonUsersFile;
    File jsonAdminFile;

    @BeforeEach
    void prepareFiles() throws IOException {
        fileService = new FileService();
        userService = new UserService(fileService);
        objectMapper = new ObjectMapper();
        writer = objectMapper.writer(new DefaultPrettyPrinter());

        final User usertest1 = new User("user1", "user1", UserRole.USER);
        final User usertest2 = new User("user2", "user2", UserRole.USER);
        List<User> userListExpected = new ArrayList<>();
        userListExpected.add(usertest1);
        userListExpected.add(usertest2);

        final User admintest1 = new User("admin1", "admin1", UserRole.USER);
        final User admintest2 = new User("admin2", "admin2", UserRole.USER);
        List<User> adminListExpected = new ArrayList<>();
        adminListExpected.add(admintest1);
        adminListExpected.add(admintest2);

        userDataFilePath = "src/main/resources/json/users/user/user.json";
        jsonUsersFile = new File(userDataFilePath);
        jsonUsersFile.delete();
        writer.writeValue(jsonUsersFile, userListExpected);

        adminDataFilePath = "src/main/resources/json/users/admin/admin.json";
        jsonAdminFile = new File(adminDataFilePath);
        jsonAdminFile.delete();
        writer.writeValue(jsonAdminFile, userListExpected);
    }

    @DisplayName("Can find a user in json file")
    @org.junit.jupiter.api.Test
    void getUserByName() throws IOException {
        final String userTestNameExpected = "user1";
        Optional<User> userFound = userService.getUserByName(userTestNameExpected);

        if(userFound.isPresent()) {
            String username = userFound.get().getUsername();
            assertEquals(userTestNameExpected, username);
        }else{
            Assertions.fail("User not found");
        }
    }

    @DisplayName("Get list of all users")
    @Test
    void getUserList() throws IOException {
/*        final User usertest1 = new User("user1", "user1", UserRole.USER);
        final User usertest2 = new User("user2", "user2", UserRole.USER);
        List<User> userListExpected = new ArrayList<>();
        userListExpected.add(usertest1);
        userListExpected.add(usertest2);

        List<User> userList = userService.getUserList();

        userList.forEach(x -> System.out.println(x.getUsername()));
        userList.forEach(x -> System.out.println(x.getPassword()));
        userListExpected.forEach(x -> System.out.println(x.getUsername()));
        userListExpected.forEach(x -> System.out.println(x.getPassword()));

        // Sort both lists using comparator based on id or whatever field is relevant
        userList.sort(Comparator.comparing(User::getUsername));
        userListExpected.sort(Comparator.comparing(User::getUsername));
        // Check if they are equal
        assertEquals(userListExpected, userList);*/
    }

    @Test
    void getAdminList() {
    }

    @Test
    void getUserAndAdminList() {
    }

    @Test
    void addNewUser() {
    }

    @Test
    void removeUser() {
    }


}