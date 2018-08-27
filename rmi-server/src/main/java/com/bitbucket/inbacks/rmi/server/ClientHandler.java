package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.protocol.Request;
import com.bitbucket.inbacks.rmi.protocol.Response;
import com.bitbucket.inbacks.rmi.server.exception.MethodNotFoundException;
import com.bitbucket.inbacks.rmi.server.exception.ServiceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@code ClientHandler} class represents
 * a request handler from the {@code Client}.
 */
class ClientHandler {
    /** The objectOutputString is used to send
     * response to the client */
    private ObjectOutputStream objectOutputStream;

    /** The objectInputStream is used to obtain
     * request from the client */
    private ObjectInputStream objectInputStream;

    /** Pool is used to create pool for threads
     * which creates threads when it is necessary */
    private ExecutorService pool = Executors.newCachedThreadPool();

    /** Logger */
    private Logger logger = LogManager.getLogger(ClientHandler.class.getName());

    /**
     * Creates a thread pool for processing requests from the {@code Client}.
     *
     * @param clientSocket client socket
     * @param properties properties from property file
     */
    public void handle(Socket clientSocket, Properties properties) {
        setStreams(clientSocket);

        while (objectInputStream != null) {
            try {
                Request request = readRequest();

                pool.execute(() -> {
                    try {
                        writeResponse(clientSocket, new Response(request.getId(),
                                new Answerer(properties.getProperty(request.getService()),
                                        request.getMethod(),
                                        request.getParameters())
                                        .getAnswer(), "none"));
                    } catch (ServiceNotFoundException e) {
                        writeResponse(clientSocket, new Response(request.getId(), e.getMessage(),
                                "service"));
                    } catch (MethodNotFoundException e) {
                        writeResponse(clientSocket, new Response(request.getId(), e.getMessage(),
                                "method"));
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                logger.warn("Problem while reading object from input stream");
                completeHandle(clientSocket);
                break;
            }
        }
    }

    /**
     * Closes {@code Socket} connection, interrupts {@code Thread}.
     *
     * @param socket socket
     */
    private void completeHandle(Socket socket) {
        try {
            socket.close();
            Thread.interrupted();
        } catch (IOException e) {
            logger.error("Completing work with client failed", e);
        }
    }

    /**
     * Causes {@link ClientHandler#setObjectOutputStream(Socket)}
     * and {@link ClientHandler#setObjectInputStream(Socket)}.
     *
     * @param socket socket
     */
    private void setStreams(Socket socket) {
        setObjectOutputStream(socket);
        setObjectInputStream(socket);
    }

    /**
     * Initialises objectOutputStream by {@code ObjectOutputStream} object.
     *
     * @param clientSocket client socket
     *
     * @see     java.io.ObjectOutputStream
     */
    private void setObjectOutputStream(Socket clientSocket) {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error("Problem while getting output stream from the client socket", e);
            completeHandle(clientSocket);
        }
    }

    /**
     * Initialises objectInputStream by {@code ObjectInputStream} object.
     *
     * @param clientSocket client socket
     *
     * @see     java.io.ObjectInputStream
     */
    private void setObjectInputStream(Socket clientSocket) {
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.error("Problem while getting input stream from the client socket", e);
            completeHandle(clientSocket);
        }
    }

    /**
     * Returns request from {@code Client}.
     *
     * @return request
     *
     * @throws IOException  thrown when there has been an Input/Output error
     * @throws ClassNotFoundException is an exception that occurs when you try
     * to load a class at run time and mentioned classes are not found in the classpath.
     */
    private Request readRequest() throws IOException, ClassNotFoundException {
        Request request = (Request) objectInputStream.readObject();
        logger.info("Request from client : {}", request);
        return request;
    }

    /**
     * Sends a response from {@code Server} to the {@code Client}.
     *
     * @param socket socket
     * @param response response
     */
    private void writeResponse(Socket socket, Response response) {
        try {
            synchronized (objectInputStream) {
                objectOutputStream.writeObject(response);
                objectOutputStream.flush();
            }
        } catch (IOException e) {
            logger.error("Problem while write response to the output stream", e);
            completeHandle(socket);
        }
    }
}