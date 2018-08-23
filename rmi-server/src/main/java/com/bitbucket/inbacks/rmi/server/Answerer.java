package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.server.exception.MethodNotFoundException;
import com.bitbucket.inbacks.rmi.server.exception.ServiceNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Answerer {
    private String service;
    private String method;
    private Object[] parameters;

    public Answerer(String service, String method, Object[] parameters) {
        this.service = service;
        this.method = method;
        this.parameters = parameters;
    }

    public Object getAnswer() throws  MethodNotFoundException, ServiceNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        if (getServiceMethod().getReturnType().getCanonicalName().equals("void")) {
            getServiceMethod().invoke(getServiceClass().newInstance(), parameters);
            return "Return type of this method is void";
        }
        return getServiceMethod().invoke(getServiceClass().newInstance(), parameters);
    }

    private Method getServiceMethod() throws ServiceNotFoundException, MethodNotFoundException {
        try {
            return getServiceClass().getDeclaredMethod(method, getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException();
        }
    }

    private Class<?>[] getParameterTypes() {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        return parameterTypes;
    }

    private Class getServiceClass() throws ServiceNotFoundException {
        try {
            return Class.forName(service);
        } catch (ClassNotFoundException | NullPointerException e) {
            throw new ServiceNotFoundException();
        }
    }
}
