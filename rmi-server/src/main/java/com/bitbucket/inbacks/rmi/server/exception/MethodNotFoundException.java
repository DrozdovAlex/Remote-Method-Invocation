package com.bitbucket.inbacks.rmi.server.exception;

/**
 * Thrown when an instance of class {@code Answerer} tries to invoke
 * {@code getAnswer} method while:
 * <ul>
 * <li>{@code method} field in {@code Answerer} has illegal name</></>.
 * <li>{@code parameters} field has illegal length</>.
 * <li>{@code parameters} field has illegal type</>.
 * </ul>
 *
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getAnswer
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getServiceMethod
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#checkMethodWithParameters
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#checkMethodName
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#checkParameters
 */
public class MethodNotFoundException extends Exception {
    /**
     * Constructs a {@code MethodNotFoundException} with no detail message.
     */
    public MethodNotFoundException() {
        super();
    }

    /**
     * Constructs a {@code MethodNotFoundException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public MethodNotFoundException(String message) {
        super(message);
    }
}
