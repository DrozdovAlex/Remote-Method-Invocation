package com.bitbucket.inbacks.rmi.protocol;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;

/**
 * This {@code Request} class represents a special message,
 * which is sent from the {@code Client} to the {@code Server}.
 */
@Data
@Getter
public class Request implements Serializable {
    /** Client id field */
    private final Long id;

    /** Service name field */
    private final String service;

    /** Method name field */
    private final String method;

    /** Parameters field */
    private final Object[] parameters;

    @Override
    public String toString() {
        return id + " " + service+" "+ method + " " + Arrays.toString(parameters);
    }
}