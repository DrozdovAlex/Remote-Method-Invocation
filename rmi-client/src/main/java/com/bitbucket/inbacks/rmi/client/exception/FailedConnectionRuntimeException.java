package com.bitbucket.inbacks.rmi.client.exception;

public class FailedConnectionRuntimeException extends RuntimeException {
    public FailedConnectionRuntimeException() {
        super();
    }

    public FailedConnectionRuntimeException(String message) {
        super(message);
    }
}
