package com.gmail.vip.alexd.rpc.server;

import com.gmail.vip.alexd.rpc.client.Client;
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

        Object actualResult = clientnew.remoteCall(1111, "Service","getHostName", new Object[]{});
        String computername=InetAddress.getLocalHost().getHostName();
        System.out.println(actualResult);
        assertEquals(computername,actualResult);
    }

    @After
    public void tearDown() throws Exception {
        clientnew.disconnect();
        servernew.disconnect();
    }

}