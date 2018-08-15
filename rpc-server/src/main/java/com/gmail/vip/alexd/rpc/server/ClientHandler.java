package com.gmail.vip.alexd.rpc.server;

import com.gmail.vip.alexd.rpc.protocol.Request;
import com.gmail.vip.alexd.rpc.protocol.Response;
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


public class ClientHandler {

    private Logger logger = LogManager.getLogger(Server.class.getName());
    ExecutorService pool = Executors.newCachedThreadPool();
    public void handle(Socket clientSocket, Properties properties){

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (objectInputStream != null){

                Request request = (Request) objectInputStream.readObject();
                logger.info("Request from client : " + request.getId() + " " + request.getService() + " " + request.getMethod());
                Response response = new Response();
                CheckingRequest checkingRequest = new CheckingRequest();
                response.setId(request.getId());
                if (properties.containsKey(request.getService())){
                    pool.execute( ()-> {
                        try {
                            response.setAnswer(checkingRequest.checking(properties.getProperty(request.getService()), request.getMethod()));
                        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                            response.setHasError(true);
                            response.setAnswer("Method not found");
                        }

                        synchronized (objectOutputStream) {
                            try {
                                objectOutputStream.writeObject(response);
                                objectOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                   });
                } else {
                    response.setHasError(true);
                    response.setAnswer("Service not found");
                    synchronized (objectOutputStream) {
                        objectOutputStream.writeObject(response);
                        objectOutputStream.flush();
                    }
                }

            }
        } catch (Exception e) {

        }
    }
}