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
     * Returns a response without the error flag enabled.
     *
     * @param requestId request id from the client
     * @param answer error flag
     * @return response
     */
    public static Response ok(Long requestId, Object answer) {
        return new Response(requestId, answer, false);
    }

    /**
     * Returns a response with the error flag enabled.
     *
     * @param requestId request id from the client
     * @param errorCode error flag
     * @return response
     */
    public static Response withError(Long requestId, String errorCode) {
        return new Response(requestId, errorCode, true);
    }
}