package pl.rstepniewski.sockets.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pl.rstepniewski.sockets.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class CommandManager {
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    private final Instant startTime = Instant.now();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CommandManager(Server server) throws IOException {
        this.server = server;
        start();
    }

    public void run() throws IOException {
        showUserInterface();
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

    private void showUserInterface() throws IOException {
        Map<String, String> jsonMap = new LinkedHashMap<>();
        jsonMap.put("SERVER MENU", "Available commands:");
        jsonMap.put("uptime", "Return server running time");
        jsonMap.put("info", "Return server version and creation date");
        jsonMap.put("help", "Return list of available commands");
        jsonMap.put("stop", "Stop server and client");

        boolean exit = false;

        while (!exit) {
            sendMessage(jsonMap, "true");

            switch (receiveAnswer().toLowerCase()) {
                case "uptime" -> showUptime();
                case "info"   -> showInfo();
                case "help"   -> showHelp();
                case "stop"   -> {
                    stop();
                    exit = true; }
                default       -> unknownCommand();
            }
        }
    }

    private void sendMessage(Map<String, String> command, String isResponseRequired) throws JsonProcessingException {
        ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("responseRequired", isResponseRequired);

        for (Map.Entry<String, String> prop : command.entrySet()) {
            jsonNode.put(prop.getKey(), prop.getValue());
        }

        String json = objectMapper.writeValueAsString(jsonNode);
        out.println(json);
    }

    private void showUptime() throws JsonProcessingException {
        Duration lifeTimeDuration = Duration.between(startTime, Instant.now());
        long seconds = lifeTimeDuration.getSeconds();

        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;

        Map<String, String> jsonMap = new LinkedHashMap<>();
        jsonMap.put("hours", Long.toString(HH));
        jsonMap.put("minutes", Long.toString(MM));
        jsonMap.put("seconds", Long.toString(SS));

        sendMessage(jsonMap, "false");
    }

    private void showInfo() throws JsonProcessingException {
        Map<String, String> jsonMap = new LinkedHashMap<>();
        jsonMap.put("server version", server.getServerVersion());
        jsonMap.put("creation date", server.getCreationDate());

        sendMessage(jsonMap, "false");
    }

    private void showHelp() throws JsonProcessingException {
        Map<String, String> jsonMap = new LinkedHashMap<>();
        jsonMap.put("uptime", "Return server running time");
        jsonMap.put("info", "Return server version and creation date");
        jsonMap.put("help", "Return list of available commands");

        sendMessage(jsonMap, "false");
    }

    private String receiveAnswer() throws IOException {
        return in.readLine();
    }

    private void unknownCommand() throws JsonProcessingException {
        Map<String, String> mapMessage = Map.of("Command", "Command unknown.");
        sendMessage(mapMessage, "false");
    }


}