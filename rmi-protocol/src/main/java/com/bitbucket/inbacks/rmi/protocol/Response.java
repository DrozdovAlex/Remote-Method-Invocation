package com.bitbucket.inbacks.rmi.protocol;

import lombok.*;

import java.io.Serializable;

/**
 * This {@code Response} class represents a special message,
 * which is sent from the {@code Server} to the {@code Client}.
 */
@Data
@Getter
public class Response implements Serializable {
    /** Client id field */
    private final Long id;

    /** Answer field */
    private final Object answer;

    /** Error flag field */
    private final boolean error;

    /**
     * Returns {@code String} object with {@code id} and
     * {@code answer} of the response.
     *
     * @return {@code String} representation of {@code Response}
     */
    @Override
    public String toString() {
        return id + " " + answer;
    }
}