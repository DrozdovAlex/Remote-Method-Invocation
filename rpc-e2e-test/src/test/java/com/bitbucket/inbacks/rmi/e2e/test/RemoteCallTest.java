package com.bitbucket.inbacks.rmi.e2e.test;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.protocol.Response;
import com.bitbucket.inbacks.rmi.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemoteCallTest {
    private static final int PORT = 4444;
    private static final String SERVICE_NAME = "Service";
    private static final String WRONG_SERVICE_NAME = SERVICE_NAME.concat("Wrong");
    private static final String MAIL = "vip.alexd@gmail.com";
    private static final String GET_MAIL_METHOD = "getMail";
    private static final String MULTIPLICATION_METHOD = "multiplication";
    private static final String WRONG_METHOD_NAME = GET_MAIL_METHOD.concat("Wrong");
    private static final String ILLEGAL_SERVICE_NAME = "Illegal service name";
    private static final String ILLEGAL_METHOD_NAME = "Illegal method name";
    private static final String ILLEGAL_NUMBER_OF_PARAMETERS = "Illegal number of parameters";
    private static final String ILLEGAL_TYPE_OF_PARAMETERS = "Illegal type of parameters";
    private static final Integer FIRST_MULTIPLIER = new Integer(10);
    private static final Integer SECOND_MULTIPLIER = new Integer(5);
    private static final Integer EXPECTED_RESULT = FIRST_MULTIPLIER * SECOND_MULTIPLIER;

    private Server server;
    private Client client;

    @Before
    public void setUp() {
        server = new Server(PORT);
        server.run();
        client = new Client("localhost", PORT);
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
    public void shouldReturnIllegalServiceNameWhenInvokeWithWrongService() {
        assertEquals(ILLEGAL_SERVICE_NAME,
                ((Response) client.remoteCall(WRONG_SERVICE_NAME, GET_MAIL_METHOD, new Object[]{}))
                        .getAnswer());
    }

    @Test
    public void shouldReturnIllegalMethodNameWhenInvokeWithWrongMethod() {
        assertEquals(ILLEGAL_METHOD_NAME,
                ((Response) client.remoteCall(SERVICE_NAME, WRONG_METHOD_NAME, new Object[]{}))
                        .getAnswer());
    }

    @Test
    public void shouldReturnIllegalNunmerOfParametersWhenInvokeWithWrongNumberOfParameters() {
        assertEquals(ILLEGAL_NUMBER_OF_PARAMETERS,
                ((Response) client.remoteCall(SERVICE_NAME, MULTIPLICATION_METHOD, new Object[]{FIRST_MULTIPLIER}))
                        .getAnswer());
    }

    @Test
    public void shouldReturnIllegalTypeOfParametersWhenInvokeWithWrongTypeOfParameters() {
        assertEquals(ILLEGAL_TYPE_OF_PARAMETERS,
                ((Response) client.remoteCall(SERVICE_NAME, MULTIPLICATION_METHOD, new Object[]{FIRST_MULTIPLIER, MAIL}))
                        .getAnswer());
    }

    @After
    public void tearDown() {
        client.disconnect();
        server.disconnect();
    }
}