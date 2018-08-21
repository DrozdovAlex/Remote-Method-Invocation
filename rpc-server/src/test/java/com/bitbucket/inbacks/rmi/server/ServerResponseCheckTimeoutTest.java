package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.CompletableFuture;

public class ServerResponseCheckTimeoutTest {
    private Server server;
    private Logger logger = LogManager.getLogger(ServerResponseCheckTimeoutTest.class);

    @Before
    public void setUp() throws Exception {
        server = new Server(4444);
        server.run();
    }

    @Test(timeout = 10000)
    public void checkReturnResponsesBeforeTimeout() throws Exception {
        Client client = new Client("localhost", 4444);
        client.run();
        for (int i = 0; i <10; i++) {
            CompletableFuture cf = CompletableFuture.runAsync(() -> {
                logger.info(client.remoteCall("Service", "getHostName", new Object[]{}));
            });
            cf.get();
        }
    }

    @After
    public void tearDown() throws Exception {
        server.disconnect();
    }
}