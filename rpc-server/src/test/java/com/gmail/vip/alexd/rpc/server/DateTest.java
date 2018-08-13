package com.gmail.vip.alexd.rpc.server;

import com.gmail.vip.alexd.rpc.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.*;

public class DateTest {
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
        Object actualResult = clientnew.remoteCall(1111, "Service","getDate", new Object[]{});
        Date date = new Date();
        System.out.println(actualResult);
        assertNotEquals(date,actualResult);
    }

    @After
    public void tearDown() throws Exception {
        clientnew.disconnect();
        servernew.disconnect();
    }

}