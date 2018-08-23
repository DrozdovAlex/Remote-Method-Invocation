package com.bitbucket.inbacks.rmi.server.exception;

public class MethodNotFoundException extends Exception {
    public MethodNotFoundException() {
        super();
    }

    public MethodNotFoundException(String message) {
        super(message);
    }
}
