package com.bitbucket.inbacks.rmi.protocol;

import java.io.Serializable;

/**
 * This {@code Request} class represents a special message,
 * which is sent from the {@code Client} to the {@code Server}.
 */
public class Request implements Serializable{

    /** Client id field */
    private final Long id;
    /** Service name field */
    private final String service;

    /** Method name field */
    private final String method;

    /** Parameters field */
    private final Object[] parameters;

    /**
     * Constructs a new {@code Request} from {@code remoteCall}
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

    /**
     * Returns the value {@link Request#Request#id}
     * @return client id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the value {@link Request#Request#service}
     * @return service name
     */
    public String getService() {
        return service;
    }

    /**
     * Returns the value {@link Request#Request#method}
     * @return method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Returns the value {@link Request#Request#parameters}
     * @return parameters
     */
    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return id + " " + service+" "+ method + " " + getStringOfParameters(parameters);
    }

    /**
     * Returns string of parameters
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
