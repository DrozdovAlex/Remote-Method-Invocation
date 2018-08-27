package com.bitbucket.inbacks.rmi.protocol;

import lombok.Getter;

import java.io.Serializable;

/**
 * This {@code Request} class represents a special message,
 * which is sent from the {@code Client} to the {@code Server}.
 */
public class Request implements Serializable {
    /** Client id field */
    @Getter
    private final Long id;

    /** Service name field */
    @Getter
    private final String service;

    /** Method name field */
    @Getter
    private final String method;

    /** Parameters field */
    @Getter
    private final Object[] parameters;

    /**
     * Constructs a new {@code Request} from {@code remoteCall}.
     *
     * @param id - client id
     * @param service - service name
     * @param method - method name
     * @param parameters - parameters
     */
    public Request(Long id, String service, String method, Object[] parameters) {
        this.id = id;
        this.service = service;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return id + " " + service+" "+ method + " " + getStringOfParameters(parameters);
    }

    /**
     * Returns string of parameters.
     *
     * @param parameters specified parameters of the {@code method}
     * @return string of parameters
     */
    private String getStringOfParameters(Object[] parameters) {
        String result = "";

        for (Object parameter : parameters) {
            result = String.format(result + " %s", parameter);
        }
        return result;
    }
}