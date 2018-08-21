package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.server.exception.MethodNotFoundException;
import com.bitbucket.inbacks.rmi.server.exception.ServiceNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Answerer {
    private String service;
    private String method;

    public Answerer(String service, String method) {
        this.service = service;
        this.method = method;
    }

    public Object getAnswer() throws  MethodNotFoundException, ServiceNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        if (getServiceMethod().getReturnType().getCanonicalName().equals("void")) {
            return "Return type of this method is void";
        }
        return getServiceMethod().invoke(getServiceClass().newInstance());
    }

    private Method getServiceMethod() throws ServiceNotFoundException, MethodNotFoundException {
        try {
            return getServiceClass().getDeclaredMethod(method);
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException();
        }
    }

    private Class getServiceClass() throws ServiceNotFoundException {
        try {
            return Class.forName(service);
        } catch (ClassNotFoundException | NullPointerException e) {
            throw new ServiceNotFoundException();
        }
    }
}
