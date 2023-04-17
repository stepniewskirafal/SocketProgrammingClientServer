package pl.rstepniewski.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pl.rstepniewski.sockets.domain.message.Message;
import pl.rstepniewski.sockets.domain.message.MessageConst;
import pl.rstepniewski.sockets.domain.message.MessageService;
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
import java.util.ArrayList;
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
    MessageService messageService = new MessageService();

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

        /*handleAdminInterface(new User("admin1", "admin1", UserRole.ADMIN)); --testing purposes*/
        User user = loginProcess();
        switch (user.getRole()) {
            case USER -> {
                handleUserInterface(user);
            }
            case ADMIN -> {
                handleAdminInterface(user);
            }
        }
    }

    private void handleAdminInterface(User user) throws IOException {
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
                case "sendMessage"    -> sendMessage(user);
                case "readMessage"    -> readMessage(user);
                default       -> unknownCommand();
            }
        }
    }

    private void handleUserInterface(User user) throws IOException {
        showUserInterface();
        while (true) {
            switch (getClientAnswer().toLowerCase()) {
                case "sendMessage"    -> sendMessage(user);
                case "readMessage"    -> readMessage(user);
                default       -> unknownCommand();
            }
        }
    }

    private void sendMessage(User user) throws IOException {
        Message message = createMessage(user);
        List<Message> messageList = new ArrayList<>();
        messageList.add(message);
        Optional<User> userByNameOptional = userService.getUserByName(message.getRecipient());
        if (userByNameOptional.isEmpty()){
            jsonNode.put("sendMessageWarning", "There is no such user in the system yet.");
            sendJsonMessage(jsonNode);
            return;
        }
        User userByName = userByNameOptional.get();
        messageService.sendMessage(userByName.getRole(), userByName.getUsername(), messageList);

        jsonNode.put("sendMessage", "The process of sending message successfully finished.");
        sendJsonMessage(jsonNode);
    }
    private Message createMessage(User user) throws IOException {
        jsonNode.put("Sending a message", "Provide a recipient name and message content");
        jsonNode.put("Recipient", "Who is your text recipient?");
        sendJsonMessage(jsonNode);
        String recipient = getClientAnswer().toLowerCase();

        jsonNode.put("Topic", "Provide a topic of your message:");
        sendJsonMessage(jsonNode);
        String topic = getClientAnswer();

        jsonNode.put("Message", "Provide a content of your message (will be trimmed to 255 characters):");
        sendJsonMessage(jsonNode);
        String content = getClientAnswer();
        content = content.substring(0, Math.min(content.length(), MessageConst.MAX_LENGTH_OF_MESSAGE.getMessageLenght()));

        return new Message(topic, content, recipient, user.getUsername());
    }

    private void readMessage(User user) throws IOException {
        Optional<List<Message>> messageListOptional = messageService.getUserMessages(user);
        if(messageListOptional.isEmpty()){
            jsonNode.put("emailBoxWarning", "There is no message to read.");
            sendJsonMessage(jsonNode);
            return;
        }

        jsonNode.put("emailBoxWarning", "Please, read your messages carefully as the below list will self-destruct after you pick the next option or close a connection.");
        List<Message> messageList = messageListOptional.get();
        for(int i=0; i<messageList.size(); i++) {
            jsonNode.put("readMessage" + (i + 1), "Message from "+ messageList.get(i).getSender());
            jsonNode.put(messageList.get(i).getSender(), messageList.get(i).getContent());
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
        jsonNode.put("readMessage", "Read a chosen message.");

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
        jsonNode.put("sendMessage", "Send a message to another User.");
        jsonNode.put("readMessage", "Read the chosen message.");

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