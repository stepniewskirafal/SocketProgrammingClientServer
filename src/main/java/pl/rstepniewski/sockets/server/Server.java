package pl.rstepniewski.sockets.server;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import pl.rstepniewski.sockets.domain.User;
import pl.rstepniewski.sockets.domain.UserRole;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private static final String SERVER_VERSION = "0.2.3";
    private static final String CREATION_DATE = "12.04.2023";
    private static final int PORT_NUMBER = 6901;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    void start() throws IOException {
        serverSocket = new ServerSocket(PORT_NUMBER);
        clientSocket = serverSocket.accept();
        System.out.println("The client has been successfully connected.");
    }

    void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
        System.out.println("The client has been successfully disconnected.");
    }

    String getServerVersion() {
        return SERVER_VERSION;
    }

    String getCreationDate() {
        return CREATION_DATE;
    }

    Socket getClientSocket() {
        return clientSocket;
    }

    public static void main(String[] args) throws IOException {
        ServerService serverService = new ServerService(new Server());
        serverService.run();
    }
}