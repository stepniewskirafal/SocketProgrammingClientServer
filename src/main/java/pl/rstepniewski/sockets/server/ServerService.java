package pl.rstepniewski.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pl.rstepniewski.sockets.domain.user.User;
import pl.rstepniewski.sockets.domain.user.UserDto;
import pl.rstepniewski.sockets.domain.user.UserRole;
import pl.rstepniewski.sockets.domain.user.UserService;
import pl.rstepniewski.sockets.file.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ServerService {
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    private final Instant startTime = Instant.now();
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectNode jsonNode = objectMapper.createObjectNode();
    FileService fileService = new FileService();
    UserService userService = new UserService(fileService);

    ServerService(Server server) throws IOException {
        this.server = server;
        start();
    }

    void run() throws IOException {
        mainLoop();
    }

    private void start() throws IOException {
        server.start();
        out = new PrintWriter(server.getClientSocket().getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(server.getClientSocket().getInputStream()));
    }

    private void stop() throws IOException {
        in.close();
        out.close();
        server.stop();
    }

    private void mainLoop() throws IOException {
        User user = loginProcess();
        switch (user.getRole()) {
            case USER -> {
                handleUserInterface();
            }
            case ADMIN -> {
                handleAdminInterface();
            }
        }
    }

    private void handleAdminInterface() throws IOException {
        showAdminInterface();
        while (true) {
            String lowerCase = getClientAnswer();
            switch (lowerCase) {
                case "uptime" -> showUptime();
                case "info"   -> showInfo();
                case "help"   -> showHelp();
                case "stop"   -> {
                    stop();
                    return;
                }
                case "listAllUsers"   -> listAllUsers();
               case "addNewUser"     -> addNewUser();
                 case "deleteUser"     -> deleteUser();
/*                case "sendMessage"    -> sendMessage();
                case "showMessageBox" -> showMessageBox();
                case "readMessage"    -> readMessage();
                case "deleteMessage"  -> deleteMessage();*/
                default       -> unknownCommand();
            }
        }
    }

    private void deleteUser() throws IOException {
        jsonNode.put("User name", "Provide new user name");
        sendJsonMessage(jsonNode);
        String userName = getClientAnswer();

        boolean responce = userService.removeUser(userName);
        if (responce) {
            jsonNode.put("addNewUser", "New user has been successfully removed");
        }else {
            jsonNode.put("addNewUser", "The process of removing a new user has failed.");
        }
        sendJsonMessage(jsonNode);
    }

    private void addNewUser() throws IOException {
        jsonNode.put("User name", "Provide new user name");
        sendJsonMessage(jsonNode);
        String userName = getClientAnswer();

        jsonNode.put("Password", "Provide new user password");
        sendJsonMessage(jsonNode);
        String password = getClientAnswer();

        String role;
        while(true) {
            jsonNode.put("Password", "Provide new user role");
            sendJsonMessage(jsonNode);
            role = getClientAnswer().toUpperCase();
            List<String> list = Arrays.stream(UserRole.values()).map(UserRole::getRoleName).toList();
            if (list.contains(role)){
                break;
            }
        }

        User userToAdd = new User(userName, password, UserRole.valueOf(role));

        boolean responce = userService.addUser(userToAdd);
        if (responce) {
            jsonNode.put("addNewUser", "New user has been successfully added");
        }else {
            jsonNode.put("addNewUser", "The process of adding a new user has failed.");
        }
        sendJsonMessage(jsonNode);
    }

    private void listAllUsers() throws IOException {
        List<User> allUserList = userService.getAllUserList();
        allUserList.stream()
            .forEach( (element) -> {
                int index = allUserList.indexOf(element);
                jsonNode.put(String.valueOf(index), element.getUsername() + " " + element.getRole());
            } );

/*        List<User> allUserList2 = userService.getAllUserList();
        allUserList2.stream()
                .forEach((index, user) -> {
                    jsonNode.put(String.valueOf(index), user.getUsername());
                });*/

        sendJsonMessage(jsonNode);
    }

    private void handleUserInterface() throws IOException {
        showUserInterface();
        while (true) {
            switch (getClientAnswer().toLowerCase()) {
/*                case "sendMessage"    -> sendMessage();
                case "showMessageBox" -> showMessageBox();
                case "readMessage"    -> readMessage();
                case "deleteMessage"  -> deleteMessage();*/
                default       -> unknownCommand();
            }
        }
    }

    private User loginProcess() throws IOException {
        Optional<User> loginAttempt;
        List<User> allUserList = userService.getAllUserList();
        showWelcomePage();
        while (true){
            UserDto userDto = getUserNameAndPassword();
            loginAttempt = allUserList
                    .stream()
                    .filter(user -> (user.getUsername().equals(userDto.getUsername())
                            && (user.getPassword().equals(userDto.getPassword()))))
                    .findFirst();
            if(!loginAttempt.isEmpty()){
                break;
            }
            getUserNameAndPassword();
        }
        return loginAttempt.get();
    }

    private void showWelcomePage() throws IOException {
        jsonNode.put("WelcomePage", "Welcome to the Message App:");
        jsonNode.put("Log in", "Provide your credentials:");
    }

    private UserDto getUserNameAndPassword() throws IOException {
        jsonNode.put("User name", "Provide your user name");
        sendJsonMessage(jsonNode);
        String userName = getClientAnswer();

        jsonNode.put("Password", "Provide your password");
        sendJsonMessage(jsonNode);
        String password = getClientAnswer();

        return new UserDto(userName, password);
    }

    private void showUserInterface() throws JsonProcessingException {
        jsonNode.put("SERVER MENU", "Options:");
        jsonNode.put("sendMessage", "Send a message to another User.");
        jsonNode.put("showMessageBox", "Present a list of your messages.");
        jsonNode.put("readMessage", "Read a chosen message.");
        jsonNode.put("deleteMessage", "Delete a chosen message.");

        sendJsonMessage(jsonNode);
    }

    private void showAdminInterface() throws JsonProcessingException {
        jsonNode.put("SERVER MENU", "Options:");
        jsonNode.put("uptime", "Return server running time");
        jsonNode.put("info", "Return server version and creation date");
        jsonNode.put("help", "Return list of available commands");
        jsonNode.put("stop", "Stop server and client");

        jsonNode.put("listAllUsers", "Show a list of all users and their roles");
        jsonNode.put("addNewUser", "Add a new user to the app");
        jsonNode.put("deleteUser","Delete user from the app");
        jsonNode.put("changeRole", "Change the user role");
        jsonNode.put("sendMessage", "Send a message to another User.");
        jsonNode.put("showMessageBox", "Present a list of your messages.");
        jsonNode.put("readMessage", "Read the chosen message.");
        jsonNode.put("deleteMessage", "Delete the chosen message.");

        sendJsonMessage(jsonNode);
    }

    private void sendJsonMessage(ObjectNode jsonNode) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(jsonNode);
        jsonNode.removeAll();
        out.println(json);
    }

    private void showUptime() throws JsonProcessingException {
        Duration lifeTimeDuration = Duration.between(startTime, Instant.now());
        long seconds = lifeTimeDuration.getSeconds();

        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        jsonNode.put("hours", Long.toString(HH));
        jsonNode.put("minutes", Long.toString(MM));
        jsonNode.put("seconds", Long.toString(SS));

        sendJsonMessage(jsonNode);
    }

    private void showInfo() throws JsonProcessingException {
        jsonNode.put("server version", server.getServerVersion());
        jsonNode.put("creation date", server.getCreationDate());

        sendJsonMessage(jsonNode);
    }

    private void showHelp() throws JsonProcessingException {
        jsonNode.put("uptime", "Return server running time");
        jsonNode.put("info", "Return server version and creation date");
        jsonNode.put("help", "Return list of available commands");

        sendJsonMessage(jsonNode);
    }

    private void unknownCommand() throws JsonProcessingException {
        jsonNode.put("Command", "Command unknown.");
        sendJsonMessage(jsonNode);
    }

    private String getClientAnswer() throws IOException {
        return in.readLine();
    }


}