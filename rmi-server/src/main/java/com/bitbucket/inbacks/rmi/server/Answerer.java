package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.server.exception.MethodNotFoundException;
import com.bitbucket.inbacks.rmi.server.exception.ServiceNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Answerer {
    private String service;
    private String method;
    private Object[] parameters;

    public Answerer(String service, String method, Object[] parameters) {
        this.service = service;
        this.method = method;
        this.parameters = parameters;
    }

    public Object getAnswer() throws  MethodNotFoundException, ServiceNotFoundException {
        try {
            if (getServiceMethod().getReturnType().getCanonicalName().equals("void")) {
                getServiceMethod().invoke(getServiceClass().newInstance(), parameters);
                return "Return type of this method is void";
            }
            return getServiceMethod().invoke(getServiceClass().newInstance(), parameters);
        } catch(InstantiationException e) {
            throw new ServiceNotFoundException("Access is denied");
        } catch (IllegalAccessException e) {
            throw new MethodNotFoundException("Access is denied");
        } catch (InvocationTargetException e) {
            throw new MethodNotFoundException("Failed when starting");
        }
    }

    private Method getServiceMethod() throws ServiceNotFoundException, MethodNotFoundException {
        checkMethodWithParameters();
        try {
            return getServiceClass().getDeclaredMethod(method, getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new MethodNotFoundException("Illegal type of parameters");
        }
    }

    private void checkMethodWithParameters() throws ServiceNotFoundException, MethodNotFoundException {
        Method[] methods = getMethodsWithEqualName();

        checkMethodName(methods);
        checkParameters(methods);
    }

    private Method[] getMethodsWithEqualName() throws ServiceNotFoundException {
        ArrayList<Method> methods = new ArrayList<>();

        for (Method m : getMethods()) {
            if (m.getName().equals(method)) {
                methods.add(m);
            }
        }
        return methods.toArray(new Method[0]);
    }

    private Method[] getMethods() throws ServiceNotFoundException {
        return getServiceClass().getMethods();
    }

    private Class getServiceClass() throws ServiceNotFoundException {
        try {
            return Class.forName(service);
        } catch (ClassNotFoundException | NullPointerException e) {
            throw new ServiceNotFoundException("Illegal service name");
        }
    }

    private void checkMethodName(Method[] methods) throws MethodNotFoundException {
        if (methods.length == 0) {
            throw new MethodNotFoundException("Illegal method name");
        }
    }

    private void checkParameters(Method[] methods) throws MethodNotFoundException {
        Method[] equalParametersNumberMethods = getEqualParameterNumberMethods(methods);

        if (equalParametersNumberMethods.length == 0) {
            throw new MethodNotFoundException("Illegal number of parameters");
        }
    }

    private Class<?>[] getParameterTypes() {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        return parameterTypes;
    }

    private Method[] getEqualParameterNumberMethods(Method[] methods) {
        ArrayList<Method> equalParametersNumberMethods = new ArrayList<>();

        for (Method m : methods) {
            if (m.getParameterCount() == parameters.length) {
                equalParametersNumberMethods.add(m);
            }
        }
        return equalParametersNumberMethods.toArray(new Method[0]);
    }
}