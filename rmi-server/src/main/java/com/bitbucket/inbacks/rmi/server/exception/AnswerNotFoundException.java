package com.bitbucket.inbacks.rmi.server.exception;

import com.bitbucket.inbacks.rmi.server.ErrorCode;

import java.io.IOException;

/**
 * Thrown when an instance of class {@code Answerer} tries to invoke
 * {@code getAnswer} method while:
 * <ul>
 * <li>{@code service} field has illegal name</li>
 * <li>{@code method} field in {@code Answerer} has illegal name</li>
 * <li>{@code parameters} field has illegal length</li>
 * <li>{@code parameters} field has illegal type</li>
 * </ul>
 *
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getAnswer
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getServiceMethod
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#checkMethodWithParameters
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getMethodsWithEqualName
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getMethods
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#getServiceClass
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#checkMethodName
 * @see     com.bitbucket.inbacks.rmi.server.Answerer#checkParameters
 */
public class AnswerNotFoundException extends IOException {
    /**
     * Constructs a {@code AnswerNotFoundException} with no detail message.
     */
    public AnswerNotFoundException() {
        super();
    }

    /**
     * Constructs a {@code AnswerNotFoundException} corresponds
     * to the special error code from {@code ErrorCode}
     *
     * @param code code of error from {@code ErrorCode}
     */
    public AnswerNotFoundException(ErrorCode code) {
        super(code.get());
    }
}
