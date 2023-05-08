package pl.rstepniewski.sockets.domain.user;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
        writer.writeValue(jsonAdminFile, adminListExpected);
    }

    @DisplayName("Can find a user in json file")
    @ParameterizedTest(name = "{arguments} should be a user name")
    @ValueSource(strings = {"user1", "user2"})
    void getUserByName(String userNameExpected) throws IOException {
        Optional<User> userFound = userService.getUserByName(userNameExpected);

        if(userFound.isPresent()) {
            String username = userFound.get().getUsername();
            assertEquals(userNameExpected, username);
        }else{
            fail("User not found");
        }
    }

    @DisplayName("Get list of all users")
    @ParameterizedTest(name = "{arguments} should be found")
    @CsvSource({
            "user1, user2"
    })
    void getUserList(String userName1,
                     String userName2
                     ) throws IOException {
        List<String> expectedUserList = Arrays.asList(userName1, userName2).stream().sorted().toList();

        List<String> actualUserList = userService.getUserList()
                .stream()
                .map(User::getUsername)
                .sorted()
                .toList();

        Assertions.assertArrayEquals(expectedUserList.toArray(), actualUserList.toArray());
    }

    @DisplayName("Get list of all users")
    @ParameterizedTest(name = "{arguments} should be found")
    @CsvSource({ "admin1, admin2" })
    void getAdminList(String userName1, String userName2) throws IOException {
            List<String> expectedUserList = Arrays.asList(userName1, userName2).stream().sorted().toList();

            List<String> actualUserList = userService.getAdminList()
                    .stream()
                    .map(User::getUsername)
                    .sorted()
                    .toList();

            Assertions.assertArrayEquals(expectedUserList.toArray(), actualUserList.toArray());
        }

    @DisplayName("Get list of all users and admins")
    @ParameterizedTest()
    @CsvSource({"user1, user2, admin1, admin2"})
    void getUserAndAdminList(String userName1, String userName2, String userName3, String userName4) throws IOException {
        List<String> expectedUserList = Arrays.asList(userName1, userName2, userName3, userName4).stream().sorted().toList();
        List<String> actualUserList = userService.getUserAndAdminList().stream().map(User::getUsername).sorted().toList();

        Assertions.assertArrayEquals(expectedUserList.toArray(), actualUserList.toArray());
    }

    @DisplayName("Add a new user")
    @ParameterizedTest(name = "Procedure returned true")
    @CsvSource({"user1, user1, USER"})
    void isAddNewUserTrue(String newUserName, String newUserPassword, UserRole newUserRole) throws IOException {
        boolean actionResult = userService.addNewUser(new User(newUserName, newUserPassword, newUserRole));
        Assertions.assertTrue(actionResult);
    }


    @DisplayName("Add a new user")
    @ParameterizedTest(name = "Find an added user")
    @CsvSource({"user3, user3, USER"})
    void isAddedNewUserExists(String newUserName, String newUserPassword, UserRole newUserRole) throws IOException {
        userService.addNewUser(new User(newUserName, newUserPassword, newUserRole));
        Optional<User> first = userService.getUserList().stream().filter(x -> x.getUsername() == newUserName).findFirst();
        Assertions.assertNotNull(first);
    }

    @DisplayName("Remove a user")
    @ParameterizedTest(name = "Procedure returned true")
    @ValueSource(strings = {"user1"})
    void removeUser(String userToRemove) throws IOException {
        boolean actionResult = userService.removeUser(userToRemove);
        Assertions.assertTrue(actionResult);
    }
}