package com.gmail.vip.alexd.rpc.demo;

import com.gmail.vip.alexd.rpc.client.Client;
import com.gmail.vip.alexd.rpc.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MainDemo {
    public static void main(String[] args) throws Exception {

        Server server = new Server();
        server.run();
        Client client = new Client();
        for (int i = 0; i <10 ; i++) {
            new Thread(new Caller(client)).start();
        }

    }
    private static class Caller implements Runnable {
        private Logger logger = LogManager.getLogger(Caller.class);
        private Client c;
        public Caller(Client c) {
            this.c = c;
        }
        public void run() {
            while(true) {
                try {
                    logger.info("Current Date is:" + c.remoteCall(5644, "Service", "getDate", new Object[]{}));
                    logger.info("Name is:" + c.remoteCall(6455, "Service", "getHostName", new Object[]{}));

                }catch (InterruptedException | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}