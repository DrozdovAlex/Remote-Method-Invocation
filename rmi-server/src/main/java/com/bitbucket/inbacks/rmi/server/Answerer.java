package com.bitbucket.inbacks.rmi.server;

import com.bitbucket.inbacks.rmi.server.exception.AnswerNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * This {@code Answerer} class provides possibility
 * to invoke methods from remote service by knowing
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
     * <p> If specified method is void then the result will be
     * {@code String} object with special message.
     *
     * @return result of {@code method} invocation with {@code parameters}
     * @throws AnswerNotFoundException if:
     *         <ul>
     *         <li>{@code service} field has illegal name</li>
     *         <li>{@code method} field in {@code Answerer} has illegal name</li>
     *         <li>{@code parameters} field has illegal length</li>
     *         <li>{@code parameters} field has illegal type</li>
     *         </ul>
     * @see     java.lang.String
     * @see     com.bitbucket.inbacks.rmi.server.exception.AnswerNotFoundException
     */
    public Object getAnswer() throws AnswerNotFoundException {
        try {
            if (getServiceMethod().getReturnType().getCanonicalName().equals("void")) {
                getServiceMethod().invoke(getServiceClass().newInstance(), parameters);
                return new String("Return type of this method is void");
            }
            return getServiceMethod().invoke(getServiceClass().newInstance(), parameters);
        } catch(InstantiationException e) {
            throw new AnswerNotFoundException(ErrorCode.SERVICE_ACCESS_IS_DENIED);
        } catch (IllegalAccessException e) {
            throw new AnswerNotFoundException(ErrorCode.METHOD_ACCESS_IS_DENIED);
        } catch (InvocationTargetException e) {
            throw new AnswerNotFoundException(ErrorCode.INVOCATION_FAILED);
        }
    }

    /**
     * Returns object of {@code Method} class corresponds to
     * the {@code method}.
     *
     * @return {@code Method} corresponds to {@code method}
     * @throws AnswerNotFoundException  if there is no service with such name or
     *         there is no method in {@code Service} with such signature
     */
    private Method getServiceMethod() throws AnswerNotFoundException {
        checkMethodWithParameters();
        try {
            return getServiceClass().getDeclaredMethod(method, getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new AnswerNotFoundException(ErrorCode.ILLEGAL_TYPE_OF_PARAMETERS);
        }
    }

    /**
     * Checks if {@code method} and {@code parameters} are legal.
     *
     * @throws AnswerNotFoundException if there is no service with such name or
     *         there is no method in {@code Service} with such signature
     */
    private void checkMethodWithParameters() throws AnswerNotFoundException {
        Method[] methods = getMethodsWithEqualName();

        checkMethodName(methods);
        checkParameters(methods);
    }

    /**
     * Returns array of {@code Method} which has the name as
     * {@code method}. Methods are extracted from the whole array
     * of {@code Method} are contained in {@code service}.
     *
     * @return array of {@code Method} with {@code method} name
     * @throws AnswerNotFoundException if there is no service with such name
     * @see java.util.ArrayList
     */
    private Method[] getMethodsWithEqualName() throws AnswerNotFoundException {
        ArrayList<Method> methods = new ArrayList<>();

        for (Method m : getMethods()) {
            if (m.getName().equals(method)) {
                methods.add(m);
            }
        }
        return methods.toArray(new Method[0]);
    }

    /**
     * Returns array of {@code Method} where every element corresponds
     * to the method from the {@code service}.
     *
     * @return array of {@code Method} corresponds to the methods from
     *         specified {@code service}
     * @throws AnswerNotFoundException if there is no service with such name
     */
    private Method[] getMethods() throws AnswerNotFoundException {
        return getServiceClass().getMethods();
    }

    /**
     * Returns object of the {@code Class} class corresponds
     * to the specified {@code service}.
     *
     * @return object of the {@code Class} corresponds to {@code service}
     * @throws AnswerNotFoundException if there is no service with such name
     */
    private Class getServiceClass() throws AnswerNotFoundException {
        try {
            return Class.forName(service);
        } catch (ClassNotFoundException | NullPointerException e) {
            throw new AnswerNotFoundException(ErrorCode.SERVICE_NOT_FOUND);
        }
    }

    /**
     * Checks if there is method in {@code service} corresponds
     * to {@code method}.
     *
     * @param methods array of {@code Method} (every element corresponds
     *                to the method in {@code service})
     * @throws AnswerNotFoundException if there is no method in {@code Service}
     *         with such name {@code method}
     */
    private void checkMethodName(Method[] methods) throws AnswerNotFoundException {
        if (methods.length == 0) {
            throw new AnswerNotFoundException(ErrorCode.METHOD_NOT_FOUND);
        }
    }

    /**
     * Checks if there is {@code Method} corresponds to {@code method}
     * with the number of parameters as in {@code parameters}
     *
     * @param methods array of {@code Method} corresponds to the methods
     *                with name {@code method}
     * @throws AnswerNotFoundException if there is no method in {@code Service}
     *         with such number of parameters as in {@code parameters}
     */
    private void checkParameters(Method[] methods) throws AnswerNotFoundException {
        Method[] equalParametersNumberMethods = getEqualParameterNumberMethods(methods);

        if (equalParametersNumberMethods.length == 0) {
            throw new AnswerNotFoundException(ErrorCode.ILLEGAL_NUMBER_OF_PARAMETERS);
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
     * Returns array of {@code Method} with the name {@code method}
     * and number of parameters is equal to the length of {@code parameters}
     *
     * @param methods array of {@code Method} with the name {@code method}
     * @return array of {@code Method} that contains methods with
     * {@code method} and number of parameters equals to {@code parameters.length}
     */
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