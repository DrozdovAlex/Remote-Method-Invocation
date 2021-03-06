package com.bitbucket.inbacks.rmi.demo;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.server.Server;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainDemo {
    public static void main(String[] args) {
        Server server = new Server(4444);
        server.run();
        Client client = new Client("localhost", 4444);
        client.run();
        for (int i = 0; i < 10; i++) {
            new Thread(new Caller(client)).start();
        }
    }

    private static class Caller implements Runnable {
        private Client c;

        public Caller(Client c) {
            this.c = c;
        }

        public void run() {
            while(true) {
                c.remoteCall("Service", "sleep", new Object[] {new Long(1000)});
                log.info("Current Date is: " + c.remoteCall("Service",
                        "getCurrentDate", new Object[]{}));
                log.info("Mail is: " + c.remoteCall("Service",
                        "getMail", new Object[]{}));
            }
        }
    }
}