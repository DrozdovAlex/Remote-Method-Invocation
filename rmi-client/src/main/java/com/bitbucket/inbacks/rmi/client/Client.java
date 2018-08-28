package com.bitbucket.inbacks.rmi.client;

import com.bitbucket.inbacks.rmi.client.exception.RemoteCallRuntimeException;
import com.bitbucket.inbacks.rmi.client.exception.FailedConnectionRuntimeException;
import com.bitbucket.inbacks.rmi.protocol.Request;
import com.bitbucket.inbacks.rmi.protocol.Response;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The {@code Client} class represents implementation of
 * client interface for calling methods from remote service.
 */
@Log4j2
public class Client {
    /** Cache the host name */
    private final String host;

    /** Cache the port number */
    private final int port;

    /** Cache the socket */
    private Socket socket;

    /** The objectInputStream is used to obtain
     * response from the server */
    private ObjectInputStream objectInputStream;

    /** The objectOutputString is used to send
     * request to the server */
    private ObjectOutputStream objectOutputStream;

    /** The responses is used to cache responses from server */
    private Map<Long, CompletableFuture<Object>> responses;

    /** The counter provides unique id
     * for every request inside session*/
    private AtomicLong counter = new AtomicLong(0L);

    /**
     * Initializes a newly created {@code Client} object
     * with specified host and port. It also initialises
     * responses with {@code ConcurrentHashMap}.
     *
     * @param host the initial value of the host
     * @param port the initial value of the port
     *
     * @see     java.util.concurrent.ConcurrentHashMap
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new ConcurrentHashMap<>();
    }

    /**
     * Prepares client to send requests to the server,
     * in other words it initialises client socket,
     * streams and start {@code Thread} to process of server response.
     *
     * @see     java.lang.Thread
     */
    public void run() {
        setSocket();
        setObjectOutputStream();
        setObjectInputStream();

        new Thread(() -> {
            while (!socket.isClosed()) {
                Response response;
                try {
                    response = (Response) objectInputStream.readObject();
                    responses.get(response.getId()).complete(response);
                } catch (SocketException e) {
                    log.warn("Socket is already closed");
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Problem while reading object from input stream");
                    break;
                }
            }
        }).start();
    }

    /**
     * Initialises client socket by {@code Socket} object.
     *
     * @see     java.net.Socket
     */
    private void setSocket() {
        log.info("{} {}", host, port);
        try {
            socket = new Socket(host, port);
        } catch(IOException e) {
            log.error("Problem while opening client socket", e);
        }
    }

    /**
     * Initialises objectOutputStream by {@code ObjectOutputStream} object.
     *
     * @see     java.io.ObjectOutputStream
     */
    public void setObjectOutputStream() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.warn("Problem with opening of output stream", e);
            disconnect();
        }
    }

    /**
     * Initialises objectInputStream by {@code ObjectInputStream} object.
     *
     * @see     java.io.ObjectInputStream
     */
    public void setObjectInputStream() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.warn("Problem with opening of input stream", e);
            disconnect();
        }
    }

    /**
     * Returns {@code method} invocation result with specified
     * {@code params}, which is contained in {@code service}.
     *
     * @param service remote service name
     * @param method method name in {@code service}
     * @param params parameters of the {@code method}
     * @return the object which is the result of the specified {@code method}
     * @exception RemoteCallRuntimeException  if:
     *             <ul>
     *             <li>There is no remote service with such name {@code service}</li>
     *             <li>Access to {@code service} is denied</li>
     *             <li>There is no method with such name {@code method}</li>
     *             <li>Length of {@code params} is illegal</li>
     *             <li>Types of {@code params} are illegal</li>
     *             <li>Access to {@code method} is denied</li>
     *             </ul>
     * @exception  FailedConnectionRuntimeException if there are problems with
     *             connection.
     */
    public Object remoteCall(String service, String method,  Object[] params) {
        Long id = counter.getAndIncrement();

        try {
            Request request = new Request(id, service, method, params);

            log.info("Your request : {}", request);

            responses.put(id, new CompletableFuture<>());
            synchronized (objectOutputStream) {
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
            }

            Response response = (Response) responses.get(id).get();
            Object answer = response.getAnswer();

            if (response.isError()) {
                throw new RemoteCallRuntimeException(answer.toString());
            }

            return answer;
        } catch (IOException e) {
            log.warn("Problem while writing object to output stream" , e);
            disconnect();
            throw new FailedConnectionRuntimeException("Problem with connection", e);
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Problem with extracting response from the map" , e);
            disconnect();
            throw new FailedConnectionRuntimeException("Problem with connection", e);
        } finally {
            responses.remove(id);
        }
    }

    /**
     * Disconnects client by closing it's {@code socket}
     */
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                responses.clear();
                socket.close();
            }
        } catch (IOException e) {
            log.error("Problem while client disconnect", e);
        }
    }
}