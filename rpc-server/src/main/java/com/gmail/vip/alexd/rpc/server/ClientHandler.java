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
        if (Thread.getDefaultUncaughtExceptionHandler() != null ) {

        }
        else {
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println("gfgf");
                    e.printStackTrace();
                }
            });
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            while (objectInputStream != null){

                Request request = (Request) objectInputStream.readObject();
                logger.info("Request from client : " + request.id + " " + request.service + " " + request.method);
                Response response = new Response();
                CheckingRequest checkingRequest = new CheckingRequest();
                response.id = request.id;
                if (properties.containsKey(request.service)){
                    pool.execute( ()-> {
                        try {
                            response.answer = checkingRequest.checking(properties.getProperty(request.service), request.method);
                        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                            e.printStackTrace();
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
                    response.answer = "Wrong service";
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