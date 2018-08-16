package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.protocol.Request;
import com.bitbucket.inbacks.rmi.protocol.Response;
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
    private CheckingRequest checkingRequest;
    private Logger logger = LogManager.getLogger(ClientHandler.class.getName());
    private ExecutorService pool = Executors.newCachedThreadPool();

    void handle(Socket clientSocket, Properties properties) throws IOException, ClassNotFoundException {
        try {
            setObjectOutputStream(clientSocket);
            setObjectInputStream(clientSocket);

            while (objectInputStream != null) {
                Request request = readRequest();

                if (properties.containsKey(request.getService())) {
                    pool.execute(() -> {
                        try {
                            checkingRequest = new CheckingRequest();
                            writeResponse(request.getId(), checkingRequest.checking(properties.getProperty(request.getService()),
                                    request.getMethod()), false);
                        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException |
                                IllegalAccessException | InstantiationException e) {
                            writeResponse(request.getId(),"Method not found", true);
                        }
                    });
                } else {
                    writeResponse(request.getId(),"Service not found", true);
                }
            }
        }catch(Exception e){
            clientSocket.close();
        }
    }

    private void setObjectOutputStream(Socket clientSocket) throws IOException {
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            clientSocket.close();
        }
    }

    private void setObjectInputStream(Socket clientSocket) throws IOException {
        try {
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            clientSocket.close();
        }
    }

    private Request readRequest() throws IOException, ClassNotFoundException {
        try {
            Request request = (Request) objectInputStream.readObject();
            logger.info("Request from client : " + request.getId() + " " + request.getService() + " " + request.getMethod());
            return request;
        } catch (IOException e) {
            Thread.interrupted();
            return null;
        }
    }

    private void writeResponse(int id, Object answer, boolean hasError) {
        synchronized (objectOutputStream) {
            try {
                objectOutputStream.writeObject(new Response(id, answer, hasError));
                objectOutputStream.flush();
            } catch (IOException e) {
                Thread.interrupted();
            }
        }
    }
}