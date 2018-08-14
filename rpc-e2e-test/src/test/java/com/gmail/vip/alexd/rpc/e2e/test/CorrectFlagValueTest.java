package com.gmail.vip.alexd.rpc.e2e.test;

import com.gmail.vip.alexd.rpc.client.Client;
import com.gmail.vip.alexd.rpc.protocol.Response;
import com.gmail.vip.alexd.rpc.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CorrectFlagValueTest {
    private Server servernew;
    private Client clientnew;

    @Before
    public void setUp() throws Exception {

        servernew = new Server();
        servernew.run();
        clientnew = new Client();
    }
    @Test
    public void setTrueForService() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(1111, "wrongservice","getDate", new Object[]{});
        boolean error = true;
        assertEquals(error,actualResult.hasError);
    }

    @Test
    public void setTrueForMethod() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(1111, "Service","wrongmethod", new Object[]{});
        boolean error = true;
        assertEquals(error,actualResult.hasError);
    }

    @Test
    public void setFalseForService() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(1111, "Service","getDate", new Object[]{});
        boolean error = false;
        assertEquals(error,actualResult.hasError);
    }

    @Test
    public void setFalseForMethod() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(1111, "Service","getDate", new Object[]{});
        boolean error = false;
        assertEquals(error,actualResult.hasError);
    }

    @After
    public void tearDown() throws Exception {
        clientnew.disconnect();
        servernew.disconnect();
    }

}