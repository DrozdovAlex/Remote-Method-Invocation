package com.gmail.vip.alexd.rpc.e2e.test;

import com.bitbucket.inbacks.rmi.client.Client;
import com.bitbucket.inbacks.rmi.protocol.Response;
import com.bitbucket.inbacks.rmi.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ServiceTest {
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
    public void assertMail() throws Exception {
        Response actualResult = (Response) client.remoteCall(222, "Service","getMail", new Object[]{});
        String mail = "vip.alexd@gmail.com";
        System.out.println(actualResult);
        assertEquals(mail,actualResult.getAnswer());
    }

    @Test
    public void assertDate() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "Service","getDate", new Object[]{});
        Date date = new Date();
        System.out.println(actualResult);
        assertNotEquals(date,actualResult.getAnswer());
    }

    @Test
    public void assertComputerName() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "Service","getHostName", new Object[]{});
        String computerName = InetAddress.getLocalHost().getHostName();
        System.out.println(actualResult);
        assertEquals(computerName,actualResult.getAnswer());
    }

    @Test
    public void checkServiceNotFound() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "bkablalb","getHostName", new Object[]{});
        String error = "Service not found";
        assertEquals(error,actualResult.getAnswer());
    }

    @Test
    public void checkMethodNotFound() throws Exception {
        Response actualResult = (Response) client.remoteCall(1111, "Service","blablabla", new Object[]{});
        String error = "Method not found";
        assertEquals(error,actualResult.getAnswer());
    }

    @After
    public void tearDown() throws Exception {
        client.disconnect();
        server.disconnect();
    }
}
