package org.example.server;

import com.google.gson.Gson;
import org.example.domain.MessageJsonMap;
import org.example.cofig.Consts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class LauncherServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static String messageBuffer;
    private MessageJsonMap messageJsonMap = new MessageJsonMap();

    public LauncherServer() {
        try {
            handleConnections();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnections() throws IOException {
        long startTime = startConnection();

        messageBuffer = in.readLine();
        while ( messageBuffer != null && !messageBuffer.equalsIgnoreCase("stop")) {
            switch (messageBuffer.toLowerCase()) {
                case "uptime" -> getUpTime(out, startTime);
                case "info" -> getServerDesr(out);
                case "help" -> getInstructions(out);
                case "stop" -> {
                    stopConnection(out, in, clientSocket, serverSocket);
                    break;
                }

            }
            out.flush();
            messageBuffer = in.readLine();
        }
    }

    private long startConnection() throws IOException {
        long startTime = System.currentTimeMillis();

        serverSocket = new ServerSocket(Consts.ClientServerPort);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        messageJsonMap.getMessageBuffer().clear();
        messageJsonMap.getMessageBuffer().put("connectionStatus", "Połączenie z serwerem zostało nawiązane");
        sendServerMessage(messageJsonMap);

        return startTime;
    }

    private void stopConnection(PrintWriter out,
                                BufferedReader in,
                                Socket socket,
                                ServerSocket ss) throws IOException {
        messageJsonMap.getMessageBuffer().clear();
        messageJsonMap.getMessageBuffer().put("connectionStatus", "Serwer wyłączony ");
        sendServerMessage(messageJsonMap);
        serverSocket.close();
    }

    public void sendServerMessage(MessageJsonMap messageJsonMap){
        Gson gson = new Gson();
        String gsonJson = gson.toJson(messageJsonMap);
        out.println(gsonJson);
        out.flush();
    }

    private void getInstructions(PrintWriter out) {
        Map<String, String> messageBuffer = messageJsonMap.getMessageBuffer();
        messageJsonMap.getMessageBuffer().clear();
        messageBuffer.put("uptime", "Zwraca czas życia serwera.");
        messageBuffer.put("info", "Zwraca numer wersji serwera, datę jego utworzenia.");
        messageBuffer.put("help", "Zwraca listę dostępnych komend z krótkim opisem.");
        messageBuffer.put("stop", "Zatrzymuje jednocześnie serwer i klienta.");
        sendServerMessage(messageJsonMap);
    }

    private void getServerDesr(PrintWriter out) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = Consts.AppCreationDate.format(formatter);
        String format = String.format("Aktualna wersja aplikacji to : %s " +
                        "serwer działa od %s"
                , Consts.AppVersion,formattedDate);
        messageJsonMap.getMessageBuffer().clear();
        messageJsonMap.getMessageBuffer().put("description", format);
        sendServerMessage(messageJsonMap);
    }

    private void getUpTime(PrintWriter out, long startTime) {
        long endTime;
        endTime = System.currentTimeMillis();

        long duration = (endTime - startTime) / 1000;
        long minutes = duration / 60;
        long seconds = duration % 60;

        String format = String.format("Czas działania serwera to %d minut i sekund %d"
                , minutes, seconds);

        messageJsonMap.getMessageBuffer().clear();
        messageJsonMap.getMessageBuffer().put("uptime", format);
        sendServerMessage(messageJsonMap);
    }
}