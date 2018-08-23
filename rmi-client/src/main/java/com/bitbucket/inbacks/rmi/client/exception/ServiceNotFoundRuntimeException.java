package com.bitbucket.inbacks.rmi.client.exception;

public class ServiceNotFoundRuntimeException extends RuntimeException {
    public ServiceNotFoundRuntimeException() {
        super();
    }

    public ServiceNotFoundRuntimeException(String message) {
        super(message);
    }
}
