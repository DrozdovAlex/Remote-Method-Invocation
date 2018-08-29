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
 */
public class RemoteCallException extends IOException {
    /**
     * Constructs a {@code RemoteCallException} corresponds
     * to the special error code from {@code ErrorCode}
     *
     * @param code code of error from {@code ErrorCode}
     */
    public RemoteCallException(ErrorCode code) {
        super(code.get());
    }

    /**
     * Constructs a {@code RemoteCallException} corresponds
     * to the special error code from {@code ErrorCode} and
     * specified error message from method
     *
     * @param code code of error from {@code ErrorCode}
     * @param extraMessage error message from method
     */
    public RemoteCallException(ErrorCode code, String extraMessage) {
        super(code.get() + ". " + extraMessage);
    }
}
