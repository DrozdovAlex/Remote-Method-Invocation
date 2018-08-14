package com.gmail.vip.alexd.rpc.e2e.test;

import com.gmail.vip.alexd.rpc.client.Client;
import com.gmail.vip.alexd.rpc.protocol.Response;
import com.gmail.vip.alexd.rpc.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.InetAddress;
import static org.junit.Assert.*;

public class HostNameTest {
    private Server servernew;
    private Client clientnew;

    @Before
    public void setUp() throws Exception {

        servernew = new Server();
        servernew.run();
        clientnew = new Client();
    }
    @Test
    public void assert_computer_name() throws Exception {

        Response actualResult = (Response) clientnew.remoteCall(1111, "Service","getHostName", new Object[]{});
        String computername=InetAddress.getLocalHost().getHostName();
        System.out.println(actualResult);
        assertEquals(computername,actualResult.answer);
    }

    @After
    public void tearDown() throws Exception {
        clientnew.disconnect();
        servernew.disconnect();
    }

}