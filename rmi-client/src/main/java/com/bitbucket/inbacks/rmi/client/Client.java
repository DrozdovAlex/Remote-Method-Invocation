package com.bitbucket.inbacks.rmi.client;

import com.bitbucket.inbacks.rmi.client.exception.FailedConnectionRuntimeException;
import com.bitbucket.inbacks.rmi.client.exception.RemoteCallRuntimeException;
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
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@code Client} class represents implementation of
 * client interface for calling methods from remote service.
 */
@Log4j2
public class Client {
    /** Server host name */
    private final String host;

    /** Server port number */
    private final int port;

    /** Socket field */
    private Socket socket;

    /** Used to obtain response from the server */
    private ObjectInputStream objectInputStream;

    /** Used to send request to the server */
    private ObjectOutputStream objectOutputStream;

    /** Used to cache responses from server */
    private Map<Long, CompletableFuture<Object>> responses;

    /** Provides unique id */
    private AtomicLong counter = new AtomicLong(0L);

    /** Used to create blocking in {@link Client#remoteCall(String, String, Object[])}. */
    private ReentrantLock locker = new ReentrantLock();

    /**
     * Initializes the client. No connection established.
     *
     * @param host value of the host
     * @param port value of the port
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new ConcurrentHashMap<>();
    }

    /**
     * Initialises client socket, streams and starts 
     * processing of server responses.
     */
    public void run() {
        initialSocket();
        initialObjectOutputStream();
        initialObjectInputStream();

        new Thread(() -> {
            while (!socket.isClosed()) {
                Response response;
                try {
                    response = (Response) objectInputStream.readObject();
                    responses.get(response.getId()).complete(response);
                } catch (SocketException e) {
                    log.warn("Socket is already closed");
                    stopFutures();
                    break;
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Problem while reading object from input stream");
                    stopFutures();
                    break;
                }
            }
        }).start();
    }

    /**
     * Initialises client socket by {@code Socket} object.
     */
    private void initialSocket() {
        log.info("{} {}", host, port);
        try {
            socket = new Socket(host, port);
        } catch(IOException e) {
            log.error("Problem while opening client socket", e);
        }
    }

    /**
     * Initialises objectOutputStream by {@code ObjectOutputStream} object.
     */
    public void initialObjectOutputStream() {
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            log.warn("Problem with opening of output stream", e);
            disconnect();
        }
    }

    /**
     * Initialises objectInputStream by {@code ObjectInputStream} object.
     */
    public void initialObjectInputStream() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.warn("Problem with opening of input stream", e);
            disconnect();
        }
    }

    private void stopFutures() {
        responses.values().stream().forEach(e -> e.cancel(true));
        responses.clear();
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

            locker.lock();
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            locker.unlock();

            Response response = (Response) responses.get(id).get();
            Object answer = response.getAnswer();

            if (response.isError()) {
                throw new RemoteCallRuntimeException((String)answer);
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
                stopFutures();
                socket.close();
            }
        } catch (IOException e) {
            log.error("Problem while client disconnect", e);
        }
    }
}