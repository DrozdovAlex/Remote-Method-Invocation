package com.bitbucket.inbacks.rmi.server.exception;

/**
 * Thrown when an instance of class {@code Answerer} tries to invoke
 * {@code getAnswer} method while {@code service} field has illegal name
 *
 * @see com.bitbucket.inbacks.rmi.server.Answerer#getAnswer
 * @see com.bitbucket.inbacks.rmi.server.Answerer#getServiceMethod
 * @see com.bitbucket.inbacks.rmi.server.Answerer#checkMethodWithParameters
 * @see com.bitbucket.inbacks.rmi.server.Answerer#getMethodsWithEqualName
 * @see com.bitbucket.inbacks.rmi.server.Answerer#getMethods
 * @see com.bitbucket.inbacks.rmi.server.Answerer#getServiceClass
 */
public class ServiceNotFoundException extends Exception {
    /**
     * Constructs a {@code ServiceNotFoundException} with no detail message.
     */
    public ServiceNotFoundException() {
        super();
    }

    /**
     * Constructs a {@code ServiceNotFoundException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }
}
