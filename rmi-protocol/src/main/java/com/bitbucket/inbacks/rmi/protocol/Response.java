package com.bitbucket.inbacks.rmi.protocol;

import lombok.Getter;

import java.io.Serializable;

/**
 * This {@code Response} class represents a special message,
 * which is sent from the {@code Server} to the {@code Client}.
 */
public class Response implements Serializable {
    /** Client id field */
    @Getter
    private final Long id;

    /** Answer field */
    @Getter
    private final Object answer;

    /** Error flag field */
    @Getter
    private final boolean error;

    /**
     * Constructs a new {@code Response} from {@code handle}.
     *
     * @param id - client id
     * @param answer - answer
     * @param error - error
     */
    public Response(Long id, Object answer, boolean error) {
        this.id = id;
        this.answer = answer;
        this.error = error;
    }

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