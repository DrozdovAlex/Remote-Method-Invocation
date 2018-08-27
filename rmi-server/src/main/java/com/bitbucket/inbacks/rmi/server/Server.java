package com.bitbucket.inbacks.rmi.server;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * The {@code Server} class represents a server,
 * that runs in multithreaded mode and waits for client requests.
 */
@Log4j2
public class Server {
    /** Cache the property file name */
    private final String PROPERTY_FILE_NAME = "/server.properties";

    /** Cache the host name */
    private ServerSocket serverSocket;

    /** The properties is used to load properties from property file */
    private Properties properties;

    /**
     * Initializes a newly created {@code Server} object
     * with specified port and causes methods {@link Server#loadProperties()}
     * and {@link Server#setServerSocket(int)}.
     *
     * @param PORT the initial value of the port
     */
    public Server(int PORT) {
        loadProperties();
        setServerSocket(PORT);
    }

    /**
     * Initialises properties with {@code Properties}.
     * Load properties from property file.
     *
     * @see     java.util.Properties
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(PROPERTY_FILE_NAME));
        } catch (IOException e) {
            log.warn("Can not extract properties from the {}", PROPERTY_FILE_NAME);
        }
    }

    /**
     * Initialises server socket by {@code ServerSocket} object.
     *
     * @param PORT the initial value of the port
     *
     * @see     java.net.ServerSocket
     */
    private void setServerSocket(int PORT) {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            log.error("Problem while opening client socket");
        }
    }

    /**
     * Creates a new {@code Thread}, responsible for accepting clients,
     * and causes method {@link Server#startClientHandler(Socket)}.
     */
    public void run() {
        new Thread(() -> {
            while (!Thread.interrupted() || !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    startClientHandler(clientSocket);
                } catch (IOException e) {
                    log.warn("Can't get client socket from server socket");
                    break;
                }
            }
        }).start();
    }

    /**
     * Creates a new {@code Thread}, responsible for
     * creating {@link ClientHandler#handle(Socket, Properties)} object
     * with {@code Socket} connection and {@code Properties} file.
     *
     * @param socket client socket
     */
    private void startClientHandler(Socket socket) {
        new Thread(() -> {
            new ClientHandler().handle(socket, properties);
        }).start();
    }

    /**
     * Disconnect {@code Server} by closing {@code ServerSocket}
     */
    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            log.error("Problem while server disconnect", e);
        }

    }
}