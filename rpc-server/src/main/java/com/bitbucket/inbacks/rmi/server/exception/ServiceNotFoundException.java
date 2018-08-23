package com.bitbucket.inbacks.rmi.server.exception;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }
}
