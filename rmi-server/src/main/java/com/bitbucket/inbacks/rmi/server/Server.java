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

    public Server(int PORT) {
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

    private void setServerSocket(int PORT) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.error("Problem while opening client socket");
        }
    }

    public void run() {
        new Thread(() -> {
            while (!Thread.interrupted() || !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    startClientHandler(clientSocket);
                } catch (IOException e) {
                    logger.warn("Can't get client socket from server socket");
                    break;
                }
            }
        }).start();
    }

    private void startClientHandler(Socket socket) {
        new Thread(() -> {
            new ClientHandler().handle(socket, properties);
        }).start();
    }

    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.error("Problem while server disconnect", e);
        }

    }
}