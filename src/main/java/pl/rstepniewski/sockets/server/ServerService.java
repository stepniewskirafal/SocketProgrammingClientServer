package pl.rstepniewski.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pl.rstepniewski.sockets.domain.User;
import pl.rstepniewski.sockets.domain.UserDto;
import pl.rstepniewski.sockets.domain.UserService;
import pl.rstepniewski.sockets.file.FileReadingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ServerService {
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    private final Instant startTime = Instant.now();
    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectNode jsonNode = objectMapper.createObjectNode();

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
            switch (receiveAnswer().toLowerCase()) {
                case "uptime" -> showUptime();
                case "info"   -> showInfo();
                case "help"   -> showHelp();
                case "stop"   -> {
                    stop();
                    return;
                }
                default       -> unknownCommand();
            }
        }
    }

    private void handleUserInterface() throws IOException {
        showUserInterface();
        while (true) {
            switch (receiveAnswer().toLowerCase()) {
                case "uptime" -> showUptime();
                case "info"   -> showInfo();
                case "help"   -> showHelp();
                case "stop"   -> {
                    stop();
                    return;
                }
                default       -> unknownCommand();
            }
        }
    }

    private User loginProcess() throws IOException {
        Optional<User> loginAttempt;
        FileReadingService fileReadingService = new FileReadingService();
        UserService userService = new UserService(fileReadingService);

        /*wywalić do jednej procki komitować jako poprawwa logowania i interface*/
        List<User> allUserList = userService.getAllUserList();

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

    private UserDto getUserNameAndPassword() throws IOException {
        jsonNode.put("Log in", "Provide your credentials:");
        jsonNode.put("User name", "Provide your user name");
        sendMessage(jsonNode);
        String userName = receiveAnswer();

        jsonNode.put("Log in", "Provide your credentials:");
        jsonNode.put("Password", "Provide your password");
        sendMessage(jsonNode);
        String password = receiveAnswer();

        return new UserDto(userName, password);
    }

    private void showUserInterface() throws JsonProcessingException {
        jsonNode.put("SERVER MENU", "Options:");
        jsonNode.put("sendMessage", "Send a message to another User.");
        jsonNode.put("showMessageBox", "Present a list of your messages.");
        jsonNode.put("readMessage", "Read a chosen message.");
        jsonNode.put("deleteMessage", "Delete a chosen message.");

        sendMessage(jsonNode);
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

        sendMessage(jsonNode);
    }

    private void sendMessage(ObjectNode jsonNode) throws JsonProcessingException {
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

        sendMessage(jsonNode);
    }

    private void showInfo() throws JsonProcessingException {
        jsonNode.put("server version", server.getServerVersion());
        jsonNode.put("creation date", server.getCreationDate());

        sendMessage(jsonNode);
    }

    private void showHelp() throws JsonProcessingException {
        jsonNode.put("uptime", "Return server running time");
        jsonNode.put("info", "Return server version and creation date");
        jsonNode.put("help", "Return list of available commands");

        sendMessage(jsonNode);
    }

    private void unknownCommand() throws JsonProcessingException {
        jsonNode.put("Command", "Command unknown.");
        sendMessage(jsonNode);
    }

    private String receiveAnswer() throws IOException {
        return in.readLine();
    }


}