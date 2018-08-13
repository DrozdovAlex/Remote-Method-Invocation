package com.gmail.vip.alexd.rpc.protocol;

import java.io.Serializable;

public class Request implements Serializable{
    public Integer id;
    public String service;
    public String method;
    Object[] param;
    @Override
    public String toString()
    {
        return id + " " + service+" "+ method + " " + param;
    }
}
