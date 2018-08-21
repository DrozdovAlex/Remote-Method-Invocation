package com.bitbucket.inbacks.rmi.e2e.test;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.protocol.Response;
import com.bitbucket.inbacks.rmi.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CorrectErrorFlagTest {
    private static final String LEGAL_SERVICE_NAME = "Service";
    private static final String LEGAL_METHOD_NAME = "getMail";
    private static final String ILLEGAL_SERVICE_NAME = LEGAL_SERVICE_NAME.concat("Wrong");
    private static final String ILLEGAL_METHOD_NAME = LEGAL_METHOD_NAME.concat("Wrong");

    private Server server;
    private Client client;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        server.run();
        client = new Client("localhost", 4444);
        client.run();
    }

    @Test
    public void shouldSetTrueForWrongServiceName() {
        assertTrue(((Response) client.remoteCall(ILLEGAL_SERVICE_NAME, LEGAL_METHOD_NAME, new Object[]{}))
                .hasError());
    }

    @Test
    public void shouldSetTrueForWrongMethodName() {
        assertTrue(((Response) client.remoteCall(LEGAL_SERVICE_NAME, ILLEGAL_METHOD_NAME, new Object[]{}))
                .hasError());
    }

    @Test
    public void shouldSetFalseForExistingServiceAndMethod() {
        assertFalse(((Response) client.remoteCall(LEGAL_SERVICE_NAME, LEGAL_METHOD_NAME, new Object[]{}))
                .hasError());
    }

    @After
    public void tearDown() throws Exception {
        client.disconnect();
        server.disconnect();
    }
}