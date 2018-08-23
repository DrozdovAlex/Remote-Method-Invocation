package com.bitbucket.inbacks.rmi.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    private Integer id;
    private Object answer;

    public Response(Integer id, Object answer) {
        this.id = id;
        this.answer = answer;
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

    @Override
    public String toString() {
        return id + " " + answer;
    }
}