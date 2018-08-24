package com.bitbucket.inbacks.rmi.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    private Long id;
    private Object answer;
    private String errorSpot;

    public Response(Long id, Object answer, String errorSpot) {
        this.id = id;
        this.answer = answer;
        this.errorSpot = errorSpot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public String getErrorSpot() {
        return errorSpot;
    }

    public void setErrorSpot(String errorSpot) {
        this.errorSpot = errorSpot;
    }

    @Override
    public String toString() {
        return id + " " + answer;
    }
}