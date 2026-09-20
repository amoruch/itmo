package com.example.server.managers;

import com.example.common.DatagramTransfer;
import com.example.common.Protocol;
import com.example.common.Protocol.Request;
import com.example.common.Protocol.Response;
import com.example.common.Protocol.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * UDP request pipeline: a dedicated {@link Thread} reads requests, each request
 * is processed in a new {@link Thread}, and responses are sent by a cached
 * thread pool.
 */
public final class DatagramServer implements AutoCloseable, Runnable {

    private static final long FRAGMENT_TIMEOUT_MILLIS = 2_500;
    private static final Logger LOGGER = LogManager.getLogger(DatagramServer.class);

    private final DatagramChannel channel;
    private final RequestHandler requestHandler;
    private final ExecutorService sendingPool = Executors.newCachedThreadPool();
    private final AtomicInteger requestNumber = new AtomicInteger();
    private final Object sendLock = new Object();
    private volatile boolean running = true;
    private Thread readerThread;

    public DatagramServer(int port, RequestHandler requestHandler) throws IOException {
        this.requestHandler = requestHandler;
        channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        LOGGER.info("UDP channel bound to port {}", port);
    }

    public void start() {
        readerThread = new Thread(this, "udp-request-reader");
        readerThread.start();
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
                LOGGER.info("Received request datagram from {}", packet.sender());
                int number = requestNumber.incrementAndGet();
                new Thread(() -> handlePacket(packet), "udp-request-processor-" + number).start();
            } catch (ClosedChannelException exception) {
                return;
            } catch (IOException exception) {
                if (running) {
                    LOGGER.error("UDP request reader error", exception);
                }
            }
        }
    }

    public void stop() {
        running = false;
        LOGGER.info("Stopping UDP server");
        try {
            channel.close();
        } catch (IOException ignored) {
            // The server is already stopping.
        }
        sendingPool.shutdown();
    }

    @Override
    public void close() {
        stop();
        joinReader();
        try {
            if (!sendingPool.awaitTermination(2, TimeUnit.SECONDS)) {
                sendingPool.shutdownNow();
            }
        } catch (InterruptedException exception) {
            sendingPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void handlePacket(DatagramTransfer.ReceivedData packet) {
        Response response = process(packet.data());
        LOGGER.info("Processed request from {} with status {}", packet.sender(), response.ok());
        try {
            sendingPool.submit(() -> sendResponse(response, packet.sender()));
        } catch (RuntimeException exception) {
            if (running) {
                LOGGER.error("Could not schedule UDP response", exception);
            }
        }
    }

    private void sendResponse(Response response, SocketAddress recipient) {
        try {
            byte[] data = Protocol.serialize(response);
            // A whole fragmented response must remain contiguous on the channel.
            synchronized (sendLock) {
                if (running) {
                    DatagramTransfer.send(channel, data, recipient);
                }
            }
            LOGGER.info("Sent response to {} with status {}", recipient, response.ok());
        } catch (IOException exception) {
            if (running) {
                LOGGER.error("Could not send UDP response", exception);
            }
        }
    }

    private Response process(byte[] data) {
        try {
            Object value = Protocol.deserialize(data, data.length);
            if (value instanceof Request request) {
                return requestHandler.handle(request);
            }
            return new Response(false, new Text("Invalid request"));
        } catch (Exception exception) {
            LOGGER.warn("Could not decode request", exception);
            return new Response(false, new Text("Bad request: " + exception.getMessage()));
        }
    }

    private void joinReader() {
        if (readerThread != null && readerThread != Thread.currentThread()) {
            try {
                readerThread.join(2_000);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
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
