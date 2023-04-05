package org.example.server;

import com.google.gson.Gson;
import org.example.domain.MessageJsonMap;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class LauncherClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static String lineFromServer;
    public LauncherClient() {
        try {
            initiateConnection();
            userInterface();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateConnection() throws IOException {
        clientSocket = new Socket("127.0.0.1", 6969);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());

        //System.out.println(in.readLine());
        lineFromServer = in.readLine();
        writeJson();
    }

    private void userInterface() throws IOException {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();
            if(line.equals("stop")){
                stopClient();
                break;
            }
            out.println(line);
            out.flush();
            lineFromServer = in.readLine();
            writeJson();
        }
    }

    private static void writeJson() {
        Gson gson = new Gson();
        MessageJsonMap messageJsonMap = gson.fromJson(lineFromServer, MessageJsonMap.class);
        messageJsonMap.getMessageBuffer()
                .entrySet()
                .stream()
                .map(x-> x.getKey() + ": \n\t " + x.getValue())
                .forEach(System.out::println);
    }

    private void stopClient() throws IOException {
        clientSocket.close();
        out.close();
    }

}

