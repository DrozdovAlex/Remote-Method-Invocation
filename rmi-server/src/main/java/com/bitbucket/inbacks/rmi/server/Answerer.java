package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.server.exception.RemoteCallException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This {@code Answerer} class provides possibility
 * to invoke methods from remote service by specified
 * service name ({@code service}), method name ({@code method})
 * and also parameters ({@code parameters}).
 */
public class Answerer {
    /** Cache service name */
    private String service;

    /** Cache method name */
    private String method;

    /** Cache parameters of the method */
    private Object[] parameters;

    /**
     * Initializes a newly created {@code Answerer} object
     * with specified service name, method name and also
     * parameters for the method.
     *
     * @param service service name
     * @param method method name
     * @param parameters method's parameters
     */
    public Answerer(String service, String method, Object[] parameters) {
        this.service = service;
        this.method = method;
        this.parameters = parameters;
    }

    /**
     * Returns the result of {@code method} invocation with
     * {@code parameters}, which belongs to {@code service}.
     *
     * @return result of {@code method} invocation with {@code parameters}
     * @throws RemoteCallException if:
     *         <ul>
     *         <li>{@code service} field has illegal name</li>
     *         <li>{@code method} field in {@code Answerer} has illegal name</li>
     *         <li>{@code parameters} field has illegal length</li>
     *         <li>{@code parameters} field has illegal type</li>
     *         </ul>
     */
    public Object getAnswer() throws RemoteCallException {
        try {
            Method serviceMethod = getServiceMethod();
            Class serviceClass = getServiceClass();
            return serviceMethod.invoke(serviceClass.newInstance(), parameters);
        } catch(InstantiationException e) {
            throw new RemoteCallException(ErrorCode.SERVICE_ACCESS_IS_DENIED);
        } catch (IllegalAccessException e) {
            throw new RemoteCallException(ErrorCode.METHOD_ACCESS_IS_DENIED);
        } catch (InvocationTargetException e) {
            throw new RemoteCallException(ErrorCode.INVOCATION_FAILED, e.getCause().getMessage());
        }
    }

    /**
     * Returns object of {@code Method} class corresponds to
     * the {@code method}.
     *
     * @return {@code Method} corresponds to {@code method}
     * @throws RemoteCallException  if there is no service with such name or
     *         there is no method in {@code Service} with such signature
     */
    private Method getServiceMethod() throws RemoteCallException {
        checkMethodWithParameters();
        try {
            return getServiceClass().getDeclaredMethod(method, getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new RemoteCallException(ErrorCode.ILLEGAL_TYPE_OF_PARAMETERS);
        }
    }

    /**
     * Checks if {@code method} and {@code parameters} are legal.
     *
     * @throws RemoteCallException if there is no service with such name or
     *         there is no method in {@code Service} with such signature
     */
    private void checkMethodWithParameters() throws RemoteCallException {
        List<Method> methods = getMethodsWithEqualName();

        checkMethodName(methods);
        checkParameters(methods);
    }

    /**
     * Returns list of {@code Method} which has the name as
     * {@code method}. Methods are extracted from the whole list
     * of {@code Method} are contained in {@code service}.
     *
     * @return list of {@code Method} with {@code method} name
     * @throws RemoteCallException if there is no service with such name
     */
    private List<Method> getMethodsWithEqualName() throws RemoteCallException {
        List<Method> methods = getMethods();

        return methods.stream().filter(m -> m.getName().equals(method)).collect(Collectors.toList());
    }

    /**
     * Returns list of {@code Method} where every element corresponds
     * to the method from the {@code service}.
     *
     * @return list of {@code Method} corresponds to the methods from
     *         specified {@code service}
     * @throws RemoteCallException if there is no service with such name
     */
    private List<Method> getMethods() throws RemoteCallException {
        Class service = getServiceClass();
        Method[] methodInArray = service.getMethods();
        List<Method> methodInList = new ArrayList<>();

        Collections.addAll(methodInList, methodInArray);
        return methodInList;
    }

    /**
     * Returns object of the {@code Class} class corresponds
     * to the specified {@code service}.
     *
     * @return object of the {@code Class} corresponds to {@code service}
     * @throws RemoteCallException if there is no service with such name
     */
    private Class getServiceClass() throws RemoteCallException {
        try {
            return Class.forName(service);
        } catch (ClassNotFoundException | NullPointerException e) {
            throw new RemoteCallException(ErrorCode.SERVICE_NOT_FOUND);
        }
    }

    /**
     * Checks if there is method in {@code service} corresponds
     * to {@code method}.
     *
     * @param methods list of {@code Method} (every element corresponds
     *                to the method in {@code service})
     * @throws RemoteCallException if there is no method in {@code Service}
     *         with such name {@code method}
     */
    private void checkMethodName(List<Method> methods) throws RemoteCallException {
        if (methods.size() == 0) {
            throw new RemoteCallException(ErrorCode.METHOD_NOT_FOUND);
        }
    }

    /**
     * Checks if there is {@code Method} corresponds to {@code method}
     * with the number of parameters as in {@code parameters}
     *
     * @param methods list of {@code Method} corresponds to the methods
     *                with name {@code method}
     * @throws RemoteCallException if there is no method in {@code Service}
     *         with such number of parameters as in {@code parameters}
     */
    private void checkParameters(List<Method> methods) throws RemoteCallException {
        List<Method> equalParametersNumberMethods = getEqualParameterNumberMethods(methods);

        if (equalParametersNumberMethods.size() == 0) {
            throw new RemoteCallException(ErrorCode.ILLEGAL_NUMBER_OF_PARAMETERS);
        }
    }

    /**
     * Returns array of types for specified {@code parameters}.
     *
     * <p> Types are located in the same sequence as corresponded
     * {@code parameters}
     *
     * @return array of {@code parameters} types
     */
    private Class<?>[] getParameterTypes() {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }
        return parameterTypes;
    }

    /**
     * Returns list of {@code Method} with the name {@code method}
     * and number of parameters is equal to the length of {@code parameters}
     *
     * @param methods list of {@code Method} with the name {@code method}
     * @return list of {@code Method} that contains methods with
     *         {@code method} and number of parameters equals to {@code parameters.length}
     */
    private List<Method> getEqualParameterNumberMethods(List<Method> methods) {
        return methods.stream().filter(m -> m.getParameterCount() == parameters.length)
                .collect(Collectors.toList());
    }
}