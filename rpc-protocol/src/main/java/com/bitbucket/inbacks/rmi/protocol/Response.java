package com.bitbucket.inbacks.rmi.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    private Integer id;
    private Object answer;
    private boolean error;

    public Response(Integer id, Object answer,boolean error) {
        this.id = id;
        this.answer = answer;
        this.error = error;
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

    public boolean hasError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return id + " " + answer;
    }
}