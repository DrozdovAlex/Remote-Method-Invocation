package com.bitbucket.inbacks.rmi.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    private Integer id;
    private Object answer;
    private boolean hasError;

    public Response(Integer id, Object answer,boolean hasError) {
        this.id = id;
        this.answer = answer;
        this.hasError = hasError;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getAnswer() {
        return answer;
    }

    public void setAnswer(Object answer) {
        this.answer = answer;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    @Override
    public String toString() {
        return id + " " + answer;
    }
}