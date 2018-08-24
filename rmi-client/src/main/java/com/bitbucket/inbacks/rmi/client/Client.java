package com.bitbucket.inbacks.rmi.client;

import com.bitbucket.inbacks.rmi.client.exception.FailedConnectionRuntimeException;
import com.bitbucket.inbacks.rmi.client.exception.MethodNotFoundRuntimeException;
import com.bitbucket.inbacks.rmi.client.exception.ServiceNotFoundRuntimeException;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
    private final String HOST;
    private final int PORT;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Map<Long,CompletableFuture<Object>> responses;
    private AtomicLong atomicLong = new AtomicLong();

    private Logger logger = LogManager.getLogger(Client.class.getName());

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        responses = new ConcurrentHashMap<>();
    }

    public void run() {
        setSocket();
        setObjectOutputStream();
        setObjectInputStream();

        new Thread(() -> {
            while (!socket.isClosed()) {
                Response response;
                try {
                    response = (Response) objectInputStream.readObject();
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

    private void setSocket() {
        logger.info(HOST + " " + PORT);
        try {
            socket = new Socket(HOST, PORT);
        } catch(IOException e) {
            logger.error("Problem while opening client socket");
        }
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
        Long id = atomicLong.getAndIncrement();
        try {
            Request request = new Request(id, service, method, params);

            logger.info("Your request : {}", request);

            responses.put(id, new CompletableFuture<>());
            synchronized (objectOutputStream) {
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
            }

            Response response = (Response) responses.get(id).get();

            switch (response.getErrorSpot()) {
                case "service":
                    throw new ServiceNotFoundRuntimeException(response.getAnswer().toString());
                case "method":
                    throw new MethodNotFoundRuntimeException(response.getAnswer().toString());
            }
            return response.getAnswer();
        } catch (IOException e) {
            logger.warn("Problem while writing object to output stream" , e);
            disconnect();
            throw new FailedConnectionRuntimeException("Problem with connection");
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Problem with extracting response from the map" , e);
            disconnect();
            throw new FailedConnectionRuntimeException("Problem with connection");
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