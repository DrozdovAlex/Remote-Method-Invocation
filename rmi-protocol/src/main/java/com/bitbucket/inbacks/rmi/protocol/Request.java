package com.bitbucket.inbacks.rmi.protocol;

import lombok.*;

import java.io.Serializable;

/**
 * This {@code Request} class represents a special message,
 * which is sent from the {@code Client} to the {@code Server}.
 */
@RequiredArgsConstructor
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