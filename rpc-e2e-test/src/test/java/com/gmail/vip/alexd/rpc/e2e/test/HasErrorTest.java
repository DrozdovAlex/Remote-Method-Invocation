package com.gmail.vip.alexd.rpc.e2e.test;

import com.gmail.vip.alexd.rpc.client.Client;
import com.gmail.vip.alexd.rpc.protocol.Response;
import com.gmail.vip.alexd.rpc.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HasErrorTest {
    private Server servernew;
    private Client clientnew;

    @Before
    public void setUp() throws Exception {

        servernew = new Server();
        servernew.run();
        clientnew = new Client();
    }
    @Test
    public void checkServiceNotFound() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(1111, "bkablalb","getHostName", new Object[]{});
        String error = "Service not found";
        assertEquals(error,actualResult.answer);
    }

    @Test
    public void checkMethodNotFound() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(1111, "Service","blablabla", new Object[]{});
        String error = "Method not found";
        assertEquals(error,actualResult.answer);
    }

    @After
    public void tearDown() throws Exception {
        clientnew.disconnect();
        servernew.disconnect();
    }

}