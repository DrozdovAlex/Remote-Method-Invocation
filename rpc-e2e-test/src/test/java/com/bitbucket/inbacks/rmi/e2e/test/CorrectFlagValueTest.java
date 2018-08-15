package com.bitbucket.inbacks.rmi.e2e.test;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.protocol.Response;
import com.bitbucket.inbacks.rmi.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CorrectFlagValueTest {
    private Server server;
    private Client client;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        server.run();
        client = new Client();
        client.run();
    }
    @Test
    public void setTrueForService() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "wrongservice","getDate", new Object[]{});
        boolean error = true;
        assertEquals(error,actualResult.isHasError());
    }

    @Test
    public void setTrueForMethod() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "Service","wrongmethod", new Object[]{});
        boolean error = true;
        assertEquals(error,actualResult.isHasError());
    }

    @Test
    public void setFalseForService() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "Service","getDate", new Object[]{});
        boolean error = false;
        assertEquals(error,actualResult.isHasError());
    }

    @Test
    public void setFalseForMethod() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "Service","getDate", new Object[]{});
        boolean error = false;
        assertEquals(error,actualResult.isHasError());
    }

    @After
    public void tearDown() throws Exception {
        client.disconnect();
        server.disconnect();
    }
}