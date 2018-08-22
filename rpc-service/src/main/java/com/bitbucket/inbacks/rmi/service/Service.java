package com.bitbucket.inbacks.rmi.service;


import java.util.Date;

public class Service {
    public void sleep(Long millis) throws InterruptedException {
        Thread.sleep(millis.longValue());
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public static String getMail() {
        return "vip.alexd@gmail.com";
    }

    public Integer multiplication(Integer number1, Integer number2) {
        return number1 * number2;
    }
}
