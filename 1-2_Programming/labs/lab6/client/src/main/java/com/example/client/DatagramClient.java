package com.example.client;

import com.example.common.DatagramTransfer;
import com.example.common.Protocol;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.DatagramChannel;

/**
 * Sends one request and waits synchronously for its UDP response.
 */
public final class DatagramClient implements AutoCloseable {

    private static final long RESPONSE_TIMEOUT_MILLIS = 2_500;

    private final DatagramChannel channel;
    private final SocketAddress server;

    public DatagramClient(String host, int port) throws IOException {
        server = new InetSocketAddress(host, port);
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
    }

    public Response request(Request request) throws Exception {
        DatagramTransfer.send(channel, Protocol.serialize(request), server);

        DatagramTransfer.ReceivedData received = DatagramTransfer.receive(
                channel, RESPONSE_TIMEOUT_MILLIS);
        if (received == null) {
            throw new SocketTimeoutException("server did not respond within 2.5 seconds");
        }

        Object value = Protocol.deserialize(received.data(), received.data().length);
        if (value instanceof Response response) {
            return response;
        }
        throw new IllegalStateException("server returned an invalid response object");
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
