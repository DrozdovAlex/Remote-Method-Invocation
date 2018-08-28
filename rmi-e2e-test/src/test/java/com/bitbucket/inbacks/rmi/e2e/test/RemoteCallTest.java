package com.bitbucket.inbacks.rmi.e2e.test;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.client.exception.RemoteCallRuntimeException;
import com.bitbucket.inbacks.rmi.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class RemoteCallTest {
    private static final int PORT = 4444;
    private static final String SERVICE_NAME = "Service";
    private static final String WRONG_SERVICE_NAME = ("Wrong service");
    private static final String MAIL = "vip.alexd@gmail.com";
    private static final String GET_MAIL_METHOD = "getMail";
    private static final String MULTIPLICATION_METHOD = "multiplication";
    private static final String WRONG_METHOD_NAME = ("Wrong method");
    private static final String ILLEGAL_SERVICE_NAME = "Illegal service name";
    private static final String ILLEGAL_METHOD_NAME = "Illegal method name";
    private static final String ILLEGAL_NUMBER_OF_PARAMETERS = "Illegal number of parameters";
    private static final String ILLEGAL_TYPE_OF_PARAMETERS = "Illegal type of parameters";
    private static final int FIRST_MULTIPLIER = 10;
    private static final int SECOND_MULTIPLIER = 5;
    private static final int EXPECTED_RESULT = FIRST_MULTIPLIER * SECOND_MULTIPLIER;

    private Server server;
    private Client client;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
                (call(SERVICE_NAME, GET_MAIL_METHOD)));
    }

    @Test
    public void shouldReturnCompositionWhenInvokeMultiplicationMethod() {
        assertEquals(EXPECTED_RESULT,
                (call(SERVICE_NAME, MULTIPLICATION_METHOD,
                        FIRST_MULTIPLIER,SECOND_MULTIPLIER)));
    }

    @Test
    public void shouldThrowRemoteCallRuntimeExceptionWhenInvokeWithWrongService() {
        thrown.expect(RemoteCallRuntimeException.class);
        thrown.expectMessage(ILLEGAL_SERVICE_NAME);
        call(WRONG_SERVICE_NAME, GET_MAIL_METHOD);
    }

    @Test
    public void shouldThrowRemoteCallRuntimeExceptionWhenInvokeWithWrongMethod() {
        thrown.expect(RemoteCallRuntimeException.class);
        thrown.expectMessage(ILLEGAL_METHOD_NAME);
        call(SERVICE_NAME, WRONG_METHOD_NAME);
    }

    @Test
    public void shouldThrowRemoteCallRuntimeExceptionWhenInvokeWithWrongNumberOfParameters() {
        thrown.expect(RemoteCallRuntimeException.class);
        thrown.expectMessage(ILLEGAL_NUMBER_OF_PARAMETERS);
        call(SERVICE_NAME, MULTIPLICATION_METHOD, FIRST_MULTIPLIER);
    }

    @Test
    public void shouldThrowRemoteCallRuntimeExceptionWhenInvokeWithWrongTypeOfParameters() {
        thrown.expect(RemoteCallRuntimeException.class);
        thrown.expectMessage(ILLEGAL_TYPE_OF_PARAMETERS);
        call(SERVICE_NAME, MULTIPLICATION_METHOD, FIRST_MULTIPLIER, MAIL);
    }

    private Object call(String service, String method, Object ...a){
        return client.remoteCall(service, method, a);
    }

    @After
    public void tearDown() {
        client.disconnect();
        server.disconnect();
    }
}