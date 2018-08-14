package com.gmail.vip.alexd.rpc.protocol;

import java.io.Serializable;

public class Response implements Serializable {
    public Integer id;
    public Object answer;
    public boolean hasError;
    @Override
    public String toString()
    {
        return id + " " + answer;
    }
}