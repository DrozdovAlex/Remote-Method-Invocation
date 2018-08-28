package com.bitbucket.inbacks.rmi.client.exception;

/**
 * Thrown when an application tries to setup the connection
 * between client and server by using {@code remoteCall} method
 * in class {@code Client} but it fails because of problems
 * with input and output streams.
 *
 * @see     com.bitbucket.inbacks.rmi.client.Client#remoteCall
 */
public class FailedConnectionRuntimeException extends RuntimeException {
    /**
     * Constructs a {@code FailedConnectionRuntimeException} with the
     * specified detail message.
     *
     * @param message the detail message.
     * @param  cause the cause (which is saved for later retrieval)
     */
    public FailedConnectionRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
