package com.bitbucket.inbacks.rmi.protocol;


import java.io.Serializable;

public class Request implements Serializable{
    private final Long id;
    private final String service;
    private final String method;
    private final Object[] parameters;

    public Request(Long id, String service, String method, Object[] parameters) {
        this.id = id;
        this.service = service;
        this.method = method;
        this.parameters = parameters;
    }

    public Long getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return id + " " + service+" "+ method + " " + getStringOfParameters(parameters);
    }

    private String getStringOfParameters(Object[] parameters) {
        String result = "";

        for (Object parameter : parameters) {
            result = String.format(result + " %s", parameter);
        }
        return result;
    }
}
