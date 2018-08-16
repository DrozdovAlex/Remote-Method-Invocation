package com.bitbucket.inbacks.rmi.demo;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainDemo {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.run();
        Client client = new Client();
        client.run();
        for (int i = 0; i < 2 ; i++) {
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
                logger.info("Current Date is:" + c.remoteCall(5644, "Service", "getDate", new Object[]{}));
                logger.info("Name is:" + c.remoteCall(6455, "Service", "getHostName", new Object[]{}));
            }
        }
    }
}