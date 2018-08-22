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

    public void sleep1000() throws InterruptedException {
        Thread.sleep(1000);
    }

    public void sleep(Long millis) throws InterruptedException {
        Thread.sleep(millis.longValue());
    }

    public Integer multiplication(Integer number1, Integer number2) {
        return number1 * number2;
    }
}
