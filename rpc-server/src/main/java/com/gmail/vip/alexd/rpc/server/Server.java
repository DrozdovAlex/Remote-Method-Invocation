package com.gmail.vip.alexd.rpc.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {

    private ServerSocket serverSocket;
    private Thread thread;
    Properties properties;

    public Server() throws IOException {
        properties = new Properties();
        properties.load(getClass().getResourceAsStream("/server.properties"));
        serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty("rpc.port")));
    }

    public void run() throws Exception {

        thread = new Thread(() -> {
            while (!Thread.interrupted() || !serverSocket.isClosed()) try {
                Socket clientSocket = serverSocket.accept();

                new Thread(() -> {

                    ClientHandler requestHandler = new ClientHandler();
                    requestHandler.handle(clientSocket, properties);
                }).start();

            } catch (IOException e) {
            }
        });
        thread.start();
    }

    public void disconnect() throws IOException, InterruptedException {
        serverSocket.close();
    }
}