package com.gmail.vip.alexd.rpc.demo;

import com.gmail.vip.alexd.rpc.client.Client;

import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Client client = new Client();
        client.remoteCall(6455, "Service", "getHostName", new Object[]{});
        client.remoteCall(6455, "Service", "getDate", new Object[]{});
        client.remoteCall(6455, "Service", "getHostName", new Object[]{});
        client.remoteCall(6455, "Service", "getDate", new Object[]{});
    }
}