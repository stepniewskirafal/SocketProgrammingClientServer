package pl.rstepniewski.sockets.server;

import pl.rstepniewski.sockets.manager.CommandManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final String SERVER_VERSION = "0.3.0";
    private static final String CREATION_DATE = "10.08.2022";
    private static final int PORT_NUMBER = 4444;
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
        CommandManager commandManager = new CommandManager(new Server());
        commandManager.run();
    }
}