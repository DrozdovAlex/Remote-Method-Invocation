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
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ClientHandler {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Logger logger = LogManager.getLogger(ClientHandler.class.getName());
    private ExecutorService pool = Executors.newCachedThreadPool();

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

    private void completeHandle(Socket socket) {
        try {
            socket.close();
            Thread.interrupted();
        } catch (IOException e) {
            logger.error("Completing work with client failed", e);
        }
    }

    private void setStreams(Socket socket) {
        setObjectOutputStream(socket);
        setObjectInputStream(socket);
    }

    private void setObjectOutputStream(Socket clientSocket) {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error("Problem while getting output stream from the client socket", e);
            completeHandle(clientSocket);
        }
    }

    private void setObjectInputStream(Socket clientSocket) {
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            logger.error("Problem while getting input stream from the client socket", e);
            completeHandle(clientSocket);
        }
    }

    private Request readRequest() throws IOException, ClassNotFoundException {
        Request request = (Request) objectInputStream.readObject();
        logger.info("Request from client : {}", request);
        return request;
    }

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