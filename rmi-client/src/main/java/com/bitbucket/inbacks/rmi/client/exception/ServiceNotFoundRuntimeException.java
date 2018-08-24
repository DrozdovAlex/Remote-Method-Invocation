package com.bitbucket.inbacks.rmi.client.exception;

/**
 * Thrown when an application tries to obtain the result
 * of method {@code remoteCall} in class {@code Client}
 * if {@code service} has illegal name.
 *
 * @see com.bitbucket.inbacks.rmi.client.Client#remoteCall
 */
public class ServiceNotFoundRuntimeException extends RuntimeException {
    /**
     * Constructs a {@code ServiceNotFoundRuntimeException} with no detail message.
     */
    public ServiceNotFoundRuntimeException() {
        super();
    }

    /**
     * Constructs a {@code ServiceNotFoundRuntimeException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public ServiceNotFoundRuntimeException(String message) {
        super(message);
    }
}
