package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.protocol.Request;
import com.bitbucket.inbacks.rmi.protocol.Response;

import lombok.extern.log4j.Log4j2;
import com.bitbucket.inbacks.rmi.server.exception.RemoteCallException;
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
@Log4j2
class ClientHandler {
    /** Used to send response to the client */
    private ObjectOutputStream objectOutputStream;

    /** Used to obtain request from the client */
    private ObjectInputStream objectInputStream;

    /** Used to create pool for threads,
     * which creates threads when it is necessary */
    private ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * Creates a thread pool for processing requests from the {@code Client}.
     *
     * @param clientSocket client socket
     * @param properties properties from property file
     */
    public void handle(Socket clientSocket, Properties properties) {
        initialStreams(clientSocket);

        while (objectInputStream != null) {
            try {
                Request request = readRequest();

                pool.execute(() -> {
                    Response response;
                    try {
                        String fullServiceName = getFullServiceName(properties, request.getService());
                        Object answer = new Answerer(fullServiceName,
                                request.getMethod(),
                                request.getParameters())
                                .getAnswer();

                        response = Response.ok(request.getId(), answer);
                    } catch (RemoteCallException e) {
                        response = Response.withError(request.getId(), e.getMessage());
                    }

                    writeResponse(clientSocket, response);
                });
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Problem while reading object from input stream");
                completeHandle(clientSocket);
                break;
            }
        }
    }

    /**
     * Causes {@link ClientHandler#initialObjectOutputStream(Socket)}
     * and {@link ClientHandler#initialObjectInputStream(Socket)}.
     *
     * @param socket socket
     */
    private void initialStreams(Socket socket) {
        initialObjectOutputStream(socket);
        initialObjectInputStream(socket);
    }

    /**
     * Initialises objectOutputStream by {@code ObjectOutputStream} object.
     *
     * @param clientSocket client socket
     */
    private void initialObjectOutputStream(Socket clientSocket) {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            log.error("Problem while getting output stream from the client socket", e);
            completeHandle(clientSocket);
        }
    }

    /**
     * Initialises objectInputStream by {@code ObjectInputStream} object.
     *
     * @param clientSocket client socket
     */
    private void initialObjectInputStream(Socket clientSocket) {
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            log.error("Problem while getting input stream from the client socket", e);
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
        log.info("Request from client : {}", request);
        return request;
    }

    private String getFullServiceName(Properties properties, String serviceName) {
        return properties.getProperty(serviceName);
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
            log.error("Problem while write response to the output stream", e);
            completeHandle(socket);
        }
    }

    /**
     * Closes {@code Socket} connection, interrupts {@code Thread}.
     *
     * @param socket socket
     */
    private void completeHandle(Socket socket) {
        try {
            pool.shutdown();
            socket.close();
            Thread.interrupted();
        } catch (IOException e) {
            log.error("Completing work with client failed", e);
        }
    }
}