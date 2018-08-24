package com.bitbucket.inbacks.rmi.client.exception;

/**
 * Thrown when an application tries to obtain the result
 * of method {@code remoteCall} in class {@code Client} if:
 * <ul>
 * <li>{@code method} has illegal name</></>.
 * <li>{@code parameters} has illegal length</>.
 * <li>{@code parameters} has illegal type</>.
 * </ul>
 *
 * @see     com.bitbucket.inbacks.rmi.client.Client#remoteCall
 */
public class MethodNotFoundRuntimeException extends RuntimeException {
    /**
     * Constructs a {@code MethodNotFoundRuntimeException} with no detail message.
     */
    public MethodNotFoundRuntimeException() {
        super();
    }

    /**
     * Constructs a {@code MethodNotFoundRuntimeException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public MethodNotFoundRuntimeException(String message) {
        super(message);
    }
}
