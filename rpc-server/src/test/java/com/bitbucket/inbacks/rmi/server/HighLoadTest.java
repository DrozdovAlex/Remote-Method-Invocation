package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import static junit.framework.TestCase.assertEquals;

public class HighLoadTest {
    private Server server;
    private Client client;
    private Client client1;
    private int remoteCallCounter;
    private Logger logger = LogManager.getLogger(HighLoadTest.class);

    @Before
    public void setUp() throws Exception {
        server = new Server();
        server.run();
    }

    @Test
    public void shouldSetTrueForAllRequestsFromOneClient() throws Exception {
        int i;
        client = new Client();
        client.run();

        for (i = 0; i <100; i++) {
            CompletableFuture cf = CompletableFuture.runAsync(() -> {
                logger.info(client.remoteCall("Service", "getHostName", new Object[]{}));
            });
            cf.get();
            remoteCallCounter++;
        }
        assertEquals(i, remoteCallCounter);
    }

    @Test
    public void shouldSetTrueForAllRequestsFromManyClients() throws Exception {
        int i;
        for (i = 0; i <100; i++) {
            CompletableFuture cf = CompletableFuture.runAsync(() -> {
                try {
                    client1 = new Client();
                    client1.run();
                    logger.info(client1.remoteCall("Service", "getDate", new Object[]{}));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            cf.get();
            remoteCallCounter++;
        }
        assertEquals(i, remoteCallCounter);
    }

    @After
    public void tearDown() throws Exception {
        server.disconnect();
    }

}