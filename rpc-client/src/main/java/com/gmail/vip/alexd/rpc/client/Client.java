package com.gmail.vip.alexd.rpc.client;

import com.gmail.vip.alexd.rpc.protocol.Request;
import com.gmail.vip.alexd.rpc.protocol.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Client {
    private Socket socket;
    private  ObjectInputStream objectInputStream;
    private  ObjectOutputStream objectOutputStream;
    private Logger logger = LogManager.getLogger(Client.class.getName());
    Properties properties = new Properties();
    private Map<Integer,CompletableFuture<Object>> responseMap = new ConcurrentHashMap<>();

    public Client() throws IOException {
        try {
            properties = new Properties();
            properties.load(getClass().getResourceAsStream("/server.properties"));
            socket = new Socket(properties.getProperty("rpc.host"), Integer.parseInt(properties.getProperty("rpc.port")));
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

                new Thread(() -> {
                    while (objectInputStream!=null) {
                    Response response = null;
                    try {
                        response = (Response) objectInputStream.readObject();
                        if (response.hasError){
                            logger.error("Error: "+ response.answer);
                            Thread.interrupted();
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        logger.info("Socket is closed");
                    }
                        responseMap.get(response.id).complete(response);
                    }
                }).start();

        }catch (Exception e){
            logger.warn("Can not connect to the server");
        }
    }

    public Object remoteCall(int id, String service, String method,  Object[] params) throws IOException, ClassNotFoundException, InterruptedException {
        try {
            Request request = new Request();
            request.id = id;
            request.service = service;
            request.method = method;
            logger.info("Your request : {} {} {}", id, service, method);
            responseMap.put(request.id, new CompletableFuture<>());
            synchronized (objectOutputStream) {
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
            }
            return responseMap.get(request.id).get();

        } catch (Exception e) {
            logger.warn("Can not use method because socket is not connected" , e);
            return null;
        }
    }

    public void disconnect() throws IOException {
        socket.close();
    }
}