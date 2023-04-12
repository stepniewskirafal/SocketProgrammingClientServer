package pl.rstepniewski.sockets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String SERVER_VERSION = "0.2.2";
    private static final String CREATION_DATE = "12.04.2023";
    private static final int PORT_NUMBER = 6901;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT_NUMBER);
        clientSocket = serverSocket.accept();
        System.out.println("The client has been successfully connected.");
    }

    public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
        System.out.println("The client has been successfully disconnected.");
    }

    public String getServerVersion() {
        return SERVER_VERSION;
    }

    public String getCreationDate() {
        return CREATION_DATE;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public static void main(String[] args) throws IOException {
        ServerService serverService = new ServerService(new Server());
        serverService.run();
    }
}