package pl.rstepniewski.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;

public class ServerService {
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    private final Instant startTime = Instant.now();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static ObjectNode jsonNode = objectMapper.createObjectNode();

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
        showUserInterface();
        while (true) {
            switch (receiveAnswer().toLowerCase()) {
                case "uptime" -> showUptime();
                case "info"   -> showInfo();
                case "help"   -> showHelp();
                case "stop"   -> {
                    stop();
                    return; }
                default       -> unknownCommand();
            }
        }
    }

    private void showUserInterface() throws JsonProcessingException {
        jsonNode.put("SERVER MENU", "Options:");
        jsonNode.put("uptime", "Return server running time");
        jsonNode.put("info", "Return server version and creation date");
        jsonNode.put("help", "Return list of available commands");
        jsonNode.put("stop", "Stop server and client");

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

    private String receiveAnswer() throws IOException {
        return in.readLine();
    }

    private void unknownCommand() throws JsonProcessingException {
        jsonNode.put("Command", "Command unknown.");
        sendMessage(jsonNode);
    }
}