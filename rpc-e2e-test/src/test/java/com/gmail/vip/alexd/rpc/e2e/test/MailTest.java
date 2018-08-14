package com.gmail.vip.alexd.rpc.e2e.test;

import com.gmail.vip.alexd.rpc.client.Client;
import com.gmail.vip.alexd.rpc.server.Server;
import com.gmail.vip.alexd.rpc.protocol.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MailTest {
    private Server servernew;
    private Client clientnew;

    @Before
    public void setUp() throws Exception {

        servernew = new Server();
        servernew.run();
        clientnew = new Client();
    }
    @Test
    public void assert_mail() throws Exception {
        Response actualResult = (Response) clientnew.remoteCall(222, "Service","getMail", new Object[]{});
        String mail ="vip.alexd@gmail.com";
        System.out.println(actualResult);
        assertEquals(mail,actualResult.answer);
    }

    @After
    public void tearDown() throws Exception {
        clientnew.disconnect();
        servernew.disconnect();
    }

}