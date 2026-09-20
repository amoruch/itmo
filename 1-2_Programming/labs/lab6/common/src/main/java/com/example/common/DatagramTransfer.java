package com.example.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.locks.LockSupport;

/**
 * Splits one serialized object into UDP datagrams and joins it back. The final
 * byte in each datagram is a continuation flag: 1 means that more packets
 * follow, 0 marks the last packet of the message.
 */
public final class DatagramTransfer {

    private static final int DATA_SIZE = 8_192;
    private static final int PACKET_SIZE = DATA_SIZE + 1;
    private static final byte MORE_PACKETS = 1;
    private static final byte LAST_PACKET = 0;
    private static final long POLL_PAUSE_NANOS = 1_000_000L;

    public static void send(DatagramChannel channel, byte[] data, SocketAddress recipient)
            throws IOException {
        int packetCount = Math.max(1, (data.length + DATA_SIZE - 1) / DATA_SIZE);

        for (int packetIndex = 0; packetIndex < packetCount; packetIndex++) {
            int start = packetIndex * DATA_SIZE;
            int length = Math.min(DATA_SIZE, data.length - start);
            byte[] packet = new byte[length + 1];
            if (length > 0) {
                System.arraycopy(data, start, packet, 0, length);
            }
            packet[length] = packetIndex == packetCount - 1 ? LAST_PACKET : MORE_PACKETS;
            sendPacket(channel, ByteBuffer.wrap(packet), recipient);
        }
    }

    private static void sendPacket(DatagramChannel channel, ByteBuffer packet, SocketAddress recipient)
            throws IOException {
        while (packet.hasRemaining()) {
            int sent = channel.isConnected() ? channel.write(packet) : channel.send(packet, recipient);
            if (sent == 0) {
                LockSupport.parkNanos(POLL_PAUSE_NANOS);
            }
        }
    }

    /**
     * Reads one complete message, waiting for its first packet until the
     * timeout.
     */
    public static ReceivedData receive(DatagramChannel channel, long timeoutMillis)
            throws IOException {
        return receive(channel, timeoutMillis, true);
    }

    /**
     * Reads a complete message only if its first packet is already available.
     * The method is used by the server's main non-blocking event loop.
     */
    public static ReceivedData receiveAvailable(DatagramChannel channel, long timeoutMillis)
            throws IOException {
        return receive(channel, timeoutMillis, false);
    }

    private static ReceivedData receive(
            DatagramChannel channel, long timeoutMillis, boolean waitForFirstPacket) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        long deadline = System.nanoTime() + timeoutMillis * 1_000_000L;
        SocketAddress sender;
        do {
            sender = receivePacket(channel, buffer);
            if (sender == null && waitForFirstPacket && System.nanoTime() < deadline) {
                LockSupport.parkNanos(POLL_PAUSE_NANOS);
            }
        } while (sender == null && waitForFirstPacket && System.nanoTime() < deadline);
        if (sender == null) {
            return null;
        }

        ByteArrayOutputStream data = new ByteArrayOutputStream();
        boolean hasMore = appendPacket(buffer, data);

        while (hasMore) {
            if (System.nanoTime() >= deadline) {
                return null;
            }

            SocketAddress nextSender = receivePacket(channel, buffer);
            if (nextSender == null) {
                LockSupport.parkNanos(POLL_PAUSE_NANOS);
                continue;
            }
            if (!sender.equals(nextSender)) {
                continue;
            }
            hasMore = appendPacket(buffer, data);
        }

        return new ReceivedData(data.toByteArray(), sender);
    }

    private static SocketAddress receivePacket(DatagramChannel channel, ByteBuffer buffer)
            throws IOException {
        buffer.clear();
        SocketAddress sender = channel.receive(buffer);
        if (sender != null) {
            buffer.flip();
        }
        return sender;
    }

    private static boolean appendPacket(ByteBuffer packet, ByteArrayOutputStream output)
            throws IOException {
        if (packet.remaining() < 1) {
            throw new IOException("UDP packet has no continuation flag");
        }

        int dataLength = packet.remaining() - 1;
        byte[] part = new byte[dataLength];
        packet.get(part);
        output.write(part);
        return packet.get() == MORE_PACKETS;
    }

    public static final class ReceivedData {

        private final byte[] data;
        private final SocketAddress sender;

        private ReceivedData(byte[] data, SocketAddress sender) {
            this.data = data;
            this.sender = sender;
        }

        public byte[] data() {
            return data;
        }

        public SocketAddress sender() {
            return sender;
        }
    }
}
