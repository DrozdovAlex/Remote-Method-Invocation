package com.bitbucket.inbacks.rmi.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ServerTest {
    private Server server;

    @Before
    public void setUp() throws Exception {
        server = new Server(4444);
    }

    @Test (expected = java.net.BindException.class)
    public void portAlreadyInUse() throws IOException {
        new Server(4444);
    }

    @After
    public void tearDown() throws Exception {
        server.disconnect();
    }

}