package com.bitbucket.inbacks.rmi.client;

import com.bitbucket.inbacks.rmi.protocol.Request;
import com.bitbucket.inbacks.rmi.protocol.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class Client {
    private final static int BOUND_FOR_ID_GENERATION = 10000000;
    private final String HOST;
    private final int PORT;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Map<Integer,CompletableFuture<Object>> responses;

    private Logger logger = LogManager.getLogger(Client.class.getName());

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        responses = new ConcurrentHashMap<>();
    }

    public void run() throws IOException {
        setSocket();
        setObjectOutputStream();
        setObjectInputStream();

        new Thread(() -> {
            while (!socket.isClosed()) {
                Response response;
                try {
                    response = (Response) objectInputStream.readObject();
                    if (response.hasError()){
                        logger.warn(response.getAnswer());
                    }
                    responses.get(response.getId()).complete(response);
                } catch (SocketException e) {
                    logger.warn("Socket is already closed");
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Problem while reading object from input stream");
                    break;
                }
            }
        }).start();
    }

    private void setSocket() throws IOException {
        logger.info(HOST + " " + PORT);
        socket = new Socket(HOST, PORT);
    }

    public void setObjectOutputStream() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.warn("Problem with opening of output stream", e);
            disconnect();
        }
    }

    public void setObjectInputStream() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.warn("Problem with opening of input stream", e);
            disconnect();
        }
    }

    public Object remoteCall(String service, String method,  Object[] params) {
        int id = new Random().nextInt(BOUND_FOR_ID_GENERATION);

        try {
            Request request = new Request(id, service, method, params);

            logger.info("Your request : {}", request);

            responses.put(id, new CompletableFuture<>());
            synchronized (objectOutputStream) {
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
            }
            return responses.get(id).get();
        } catch (IOException e) {
            logger.warn("Problem while writing object to output stream" , e);
            disconnect();
            return new Response(id, "Problem with connection", true);
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Problem with extracting response from the map" , e);
            disconnect();
            return new Response(id, "Problem with connection", true);
        } finally {
            responses.remove(id);
        }
    }

    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("Problem while client disconnect", e);
        }
    }
}