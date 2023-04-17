package pl.rstepniewski.sockets.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private static final String LOCAL_HOST = "localhost";
    private static final int PORT_NUMBER = 6900;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startConnection(LOCAL_HOST, PORT_NUMBER);

        client.processCommunication();
    }

    private void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("Successfully established connection with the server.");
    }

    private void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        System.out.println("Successfully disconnected.");
    }

    private void processCommunication() throws IOException {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String messageFromServer = in.readLine();
            if (messageFromServer != null) {
                printMessageFromServer(messageFromServer);
                String command = scanner.nextLine();
                out.println(command);
            } else {
                stopConnection();
                return;
            }
        }
    }

    private void printMessageFromServer(String messageFromServer) throws JsonProcessingException {
        Map<String, String> mappedMessageFromServer = objectMapper.readValue(messageFromServer, Map.class);

        String json;
        if (mappedMessageFromServer.size() > 1) {
            json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mappedMessageFromServer);
        } else {
            json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mappedMessageFromServer.values());
        }

        System.out.println(json);
    }

}