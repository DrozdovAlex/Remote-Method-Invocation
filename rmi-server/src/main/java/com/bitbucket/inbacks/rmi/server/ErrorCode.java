package com.bitbucket.inbacks.rmi.server;

/**
 * Enumeration of all possible error codes which
 * correspond to specified errors
 */
public enum ErrorCode {

    SERVICE_NOT_FOUND("Illegal service name"),
    METHOD_NOT_FOUND("Illegal method name"),
    ILLEGAL_NUMBER_OF_PARAMETERS("Illegal number of parameters"),
    ILLEGAL_TYPE_OF_PARAMETERS("Illegal type of parameters"),
    SERVICE_ACCESS_IS_DENIED("Service access is denied"),
    METHOD_ACCESS_IS_DENIED("Method access is denied"),
    INVOCATION_FAILED("Error while method is working");

    /** Error message field */
    private String errorMessage;

    /**
     * Initializes a newly created {@code ErrorCode} object
     * with specified error message.
     *
     * @param errorMessage current error
     */
    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Returns message with error
     *
     * @return error message
     */
    public String get() {
        return errorMessage;
    }
}
