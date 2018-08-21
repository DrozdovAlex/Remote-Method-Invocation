package com.bitbucket.inbacks.rmi.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    private final String PROPERTY_FILE_NAME = "/server.properties";

    private ServerSocket serverSocket;
    private Properties properties;

    private Logger logger = LogManager.getLogger(Server.class.getName());

    public Server(int PORT) throws IOException {
        loadProperties();
        setServerSocket(PORT);
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(PROPERTY_FILE_NAME));
        } catch (IOException e) {
            logger.warn("Can not extract properties from the {}", PROPERTY_FILE_NAME);
        }
    }

    private void setServerSocket(int PORT) throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    public void run() {
        new Thread(() -> {
            while (!Thread.interrupted() || !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    startClientHandler(clientSocket);
                } catch (IOException e) {
                    //logger.error("Can't get client socket from server socket");
                }
            }
        }).start();
    }

    private void startClientHandler(Socket socket) {
        new Thread(() -> {
            new ClientHandler().handle(socket, properties);
        }).start();
    }

    public void disconnect() throws IOException {
        serverSocket.close();
    }
}