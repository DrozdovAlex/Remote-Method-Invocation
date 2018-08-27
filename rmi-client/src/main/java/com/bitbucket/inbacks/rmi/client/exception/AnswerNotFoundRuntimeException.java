package com.bitbucket.inbacks.rmi.client.exception;

/**
 * Thrown when an application tries to obtain the result
 * of method {@code remoteCall} in class {@code Client} if:
 *
 * <ul>
 * <li>There is no remote service with such name {@code service}</li>
 * <li>Access to {@code service} is denied</li>
 * <li>There is no method with such name {@code method}</li>
 * <li>Length of {@code params} is illegal</li>
 * <li>Types of {@code params} are illegal</li>
 * <li>Access to {@code method} is denied</li>
 * </ul>
 *
 * @see     com.bitbucket.inbacks.rmi.client.Client#remoteCall
 */
public class AnswerNotFoundRuntimeException extends RuntimeException {
    /**
     * Constructs a {@code AnswerNotFoundRuntimeException} with
     * no detail message.
     */
    public AnswerNotFoundRuntimeException() {
        super();
    }

    /**
     * Constructs a {@code AnswerNotFoundRuntimeException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public AnswerNotFoundRuntimeException(String message) {
        super(message);
    }
}
