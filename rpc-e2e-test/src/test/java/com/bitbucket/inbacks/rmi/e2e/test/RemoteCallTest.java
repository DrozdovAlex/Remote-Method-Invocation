package com.bitbucket.inbacks.rmi.e2e.test;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.protocol.Response;
import com.bitbucket.inbacks.rmi.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RemoteCallTest {
    private static final String SERVICE_NAME = "Service";
    private static final String WRONG_SERVICE_NAME = SERVICE_NAME.concat("Wrong");
    private static final String MAIL = "vip.alexd@gmail.com";
    private static final String GET_MAIL_METHOD = "getMail";
    private static final String MULTIPLICATION_METHOD = "multiplication";
    private static final String WRONG_METHOD_NAME = GET_MAIL_METHOD.concat("Wrong");
    private static final String SERVICE_NOT_FOUND = "Service not found";
    private static final String METHOD_NOT_FOUND = "Method not found";
    private static final Integer FIRST_MULTIPLIER = new Integer(10);
    private static final Integer SECOND_MULTIPLIER = new Integer(5);
    private static final Integer EXPECTED_RESULT = FIRST_MULTIPLIER * SECOND_MULTIPLIER;

    private Server server;
    private Client client;

    @Before
    public void setUp() throws Exception {
        server = new Server(4444);
        server.run();
        client = new Client("localhost", 4444);
        client.run();
    }

    @Test
    public void shouldReturnMailWhenInvokeGetMailMethod() {
        assertEquals(MAIL,
                ((Response) client.remoteCall(SERVICE_NAME, GET_MAIL_METHOD, new Object[]{}))
                        .getAnswer());
    }

    @Test
    public void shouldReturnCompositionWhenInvokeMultiplicationMethod() {
        assertEquals(EXPECTED_RESULT,
                ((Response) client.remoteCall(SERVICE_NAME, MULTIPLICATION_METHOD, new Object[]{FIRST_MULTIPLIER,SECOND_MULTIPLIER}))
                        .getAnswer());
    }

    @Test
    public void shouldReturnServiceNotFoundWhenInvokeWithWrongService() {
        assertEquals(SERVICE_NOT_FOUND,
                ((Response) client.remoteCall(WRONG_SERVICE_NAME, GET_MAIL_METHOD, new Object[]{}))
                        .getAnswer());
    }

    @Test
    public void shouldReturnMethodNotFoundWhenInvokeWithWrongMethod() {
        assertEquals(METHOD_NOT_FOUND,
                ((Response) client.remoteCall(SERVICE_NAME, WRONG_METHOD_NAME, new Object[]{}))
                        .getAnswer());
    }

    @After
    public void tearDown() throws IOException {
        client.disconnect();
        server.disconnect();
    }
}