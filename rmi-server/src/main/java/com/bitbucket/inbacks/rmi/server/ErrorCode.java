package com.bitbucket.inbacks.rmi.server;

/**
 * Enumeration of all possible error codes which
 * correspond to specified errors
 */
public enum ErrorCode {
    SERVICE_NOT_FOUND {
        public  String get() { return new String("Illegal service name"); }
    },
    METHOD_NOT_FOUND {
        public  String get() { return new String("Illegal method name"); }
    },
    ILLEGAL_NUMBER_OF_PARAMETERS {
        public  String get() { return new String("Illegal number of parameters"); }
    },
    ILLEGAL_TYPE_OF_PARAMETERS {
        public  String get() { return new String("Illegal type of parameters"); }
    },
    SERVICE_ACCESS_IS_DENIED {
        public  String get() { return new String("Service access is denied"); }
    },
    METHOD_ACCESS_IS_DENIED {
        public  String get() { return new String("Method access is denied"); }
    },
    INVOCATION_FAILED {
        public  String get() { return new String("Failed when starting"); }
    };

    public abstract String get();
}
