package com.bitbucket.inbacks.rmi.service;

import java.util.Date;

/**
 * This {@code Service} class represents a set of specific methods
 */
public class Service {
    /**
     * Sends a thread to sleep for a certain time
     * @param millis - sleeping time
     * @throws InterruptedException - if you catch this exception,
     * it means that the thread has been interrupted
     */
    public void sleep(Long millis) throws InterruptedException {
        Thread.sleep(millis.longValue());
    }

    /**
     * Returns the current date
     * @return current date
     */
    public Date getCurrentDate() {
        return new Date();
    }

    /**
     * Returns the mail
     * @return mail
     */
    public static String getMail() {
        return "vip.alexd@gmail.com";
    }

    /**
     * Returns the multiplication of numbers
     * @param number1 - first multiplier
     * @param number2 - second multiplier
     * @return multiplication of numbers
     */
    public Integer multiplication(Integer number1, Integer number2) {
        return number1 * number2;
    }
}