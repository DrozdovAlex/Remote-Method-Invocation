package com.bitbucket.inbacks.rmi.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    private final String PROPERTY_FILE_NAME = "/server.properties";
    private final String PORT = "rpc.port";

    private Logger logger = LogManager.getLogger(Server.class.getName());

    private ServerSocket serverSocket;
    private Thread thread;
    private Properties properties;
    private ClientHandler requestHandler;

    public Server() throws IOException {
        loadProperties();
        setServerSocket();
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(PROPERTY_FILE_NAME));
        } catch (IOException e) {
            logger.warn("Can not extract properties from the {}", PROPERTY_FILE_NAME);
        }
    }

    private void setServerSocket() throws IOException {
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