package com.bitbucket.inbacks.rmi.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

public class Server {
    private final String PROPERTY_FILE_NAME = "/server.properties";
    private final String PORT = "rpc.port";

    private ServerSocket serverSocket;
    private Properties properties;

    private Logger logger = LogManager.getLogger(Server.class.getName());

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

    public void run() {
        new Thread(() -> {
            while (!Thread.interrupted() || !serverSocket.isClosed()) {
                startClientHandler();
            }
        }).start();
    }

    private void startClientHandler() {
        try {
            new ClientHandler().handle(getClientSocket(), properties);
        } catch (IOException e) {
            Thread.interrupted();
        }
    }

    private Socket getClientSocket() throws SocketException {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            logger.error("Can't get client socket from server socket");
            throw new SocketException();
        }
    }

    public void disconnect() throws IOException {
        serverSocket.close();
    }
}