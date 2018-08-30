package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HighLoadTest {
    private final static int NUMBER_OF_CLIENTS = 100;
    private final static int NUMBER_IF_REQUESTS_PER_CLIENT = 5;
    private final static int PORT = 4444;

    private Server server;
    private Logger logger = LogManager.getLogger(HighLoadTest.class);

    @Before
    public void setUp() {
        server = new Server(PORT);
        server.run();
    }

    @Test
    public void shouldHandleRequestsFromManyClients() throws InterruptedException,
            ExecutionException {
        CompletableFuture[] futures = new CompletableFuture[NUMBER_OF_CLIENTS];

        for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                Client client = new Client("localhost", PORT);
                client.run();
                for (int j = 0; j < NUMBER_IF_REQUESTS_PER_CLIENT; j++) {
                    logger.info(client.remoteCall("Service",
                            "getCurrentDate", new Object[]{}));
                }
            });
        }
        CompletableFuture.allOf(futures).thenAccept(v -> { }).get();
    }

    @After
    public void tearDown() {
        server.disconnect();
    }
}