package com.example.server;

import com.example.common.DatagramTransfer;
import com.example.common.Protocol;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.server.managers.ServerCommandManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

/**
 * Receives UDP requests on a dedicated thread and sends command responses.
 */
final class DatagramServer implements AutoCloseable, Runnable {

    private static final long FRAGMENT_TIMEOUT_MILLIS = 2_500;

    private final DatagramChannel channel;
    private final ServerCommandManager commandManager;
    private volatile boolean running = true;
    private Thread worker;

    DatagramServer(int port, ServerCommandManager commandManager) throws IOException {
        this.commandManager = commandManager;
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
    }

    void start() {
        worker = new Thread(this, "udp-server");
        worker.start();
    }

    @Override
    public void run() {
        while (running) {
            try {
                DatagramTransfer.ReceivedData packet = DatagramTransfer.receiveAvailable(
                        channel, FRAGMENT_TIMEOUT_MILLIS);
                if (packet == null) {
                    pauseBriefly();
                    continue;
                }

                Response response = process(packet.data());
                DatagramTransfer.send(channel, Protocol.serialize(response), packet.sender());
            } catch (ClosedChannelException exception) {
                return;
            } catch (IOException exception) {
                if (running) {
                    System.err.println("UDP server error: " + exception.getMessage());
                }
            }
        }
    }

    void stop() {
        running = false;
        try {
            channel.close();
        } catch (IOException ignored) {
            // The server is already stopping.
        }
    }

    @Override
    public void close() {
        stop();
        if (worker != null && worker != Thread.currentThread()) {
            try {
                worker.join(2_000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Response process(byte[] data) {
        try {
            Object value = Protocol.deserialize(data, data.length);
            if (value instanceof Request request) {
                return commandManager.handle(request);
            }
            return new Response(false, "Invalid request");
        } catch (Exception exception) {
            return new Response(false, "Bad request: " + exception.getMessage());
        }
    }

    private static void pauseBriefly() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
