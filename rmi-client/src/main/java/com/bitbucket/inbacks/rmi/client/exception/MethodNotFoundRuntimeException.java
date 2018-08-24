package com.bitbucket.inbacks.rmi.client.exception;

public class MethodNotFoundRuntimeException extends RuntimeException {
    public MethodNotFoundRuntimeException() {
        super();
    }

    public MethodNotFoundRuntimeException(String message) {
        super(message);
    }
}
