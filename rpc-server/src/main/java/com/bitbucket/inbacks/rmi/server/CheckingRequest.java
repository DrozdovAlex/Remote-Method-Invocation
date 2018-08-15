package com.bitbucket.inbacks.rmi.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CheckingRequest {
    public Object checking(String serviceName, String methodName) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class serviceClass = Class.forName(serviceName);
        Method method = serviceClass.getDeclaredMethod(methodName);
        Object obj = method.invoke(serviceClass.newInstance());
        return obj;
    }
}
