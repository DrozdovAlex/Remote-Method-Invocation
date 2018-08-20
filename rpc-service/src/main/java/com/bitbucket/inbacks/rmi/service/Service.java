package com.bitbucket.inbacks.rmi.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Service {

    public Date getDate() {
        return new Date();
    }

    public String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    public String getMail() {
        return "vip.alexd@gmail.com";
    }

    public String getSleep() throws InterruptedException {
        Thread.sleep(1000);
        return null;
    }
}
