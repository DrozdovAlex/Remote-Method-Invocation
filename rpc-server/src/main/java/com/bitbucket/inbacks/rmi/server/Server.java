package com.bitbucket.inbacks.rmi.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    private final String PROPERTY_FILE_NAME = "/server.properties";
    private final String PORT = "rpc.port";
    private ServerSocket serverSocket;
    private Thread thread;
    private Properties properties;
    private ClientHandler requestHandler;

    public Server() throws IOException {
        properties = new Properties();
        properties.load(getClass().getResourceAsStream(PROPERTY_FILE_NAME));
        serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty(PORT)));
    }

    public void run() throws Exception {

        thread = new Thread(() -> {
            while (!Thread.interrupted() || !serverSocket.isClosed())
                try{
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> {
                        requestHandler = new ClientHandler();
                        try {
                            requestHandler.handle(clientSocket, properties);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Thread.interrupted();
                        }
                    }).start();
                } catch (IOException e) {
                    Thread.interrupted();
                }
        });
        thread.start();
    }

    public void disconnect() throws IOException, InterruptedException {
        serverSocket.close();
    }
}