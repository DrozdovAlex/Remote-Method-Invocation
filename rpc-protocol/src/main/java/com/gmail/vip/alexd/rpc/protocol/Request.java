package com.gmail.vip.alexd.rpc.protocol;

import java.io.Serializable;

public class Request implements Serializable{
    private final Integer id;
    private final String service;
    private final String method;
    private final Object[] param;

    public Request(Integer id, String service, String method, Object[] param) {
        this.id = id;
        this.service = service;
        this.method = method;
        this.param = param;
    }

    public Integer getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParam() {
        return param;
    }

    @Override
    public String toString() {
        return id + " " + service+" "+ method + " " + param;
    }
}
