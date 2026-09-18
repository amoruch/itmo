import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Урок 2.15 — сетевой обмен через NIO каналы.
 */
public class App {

    public static void main(String[] args) throws Exception {
        udpNioServerOnce();
        udpNioClient();

        tcpNioServerOnce();
        tcpNioClient();
    }

    // --- UDP через DatagramChannel ------------------------------------------

    /** UDP-сервер на канале: bind(local) + receive(buf). */
    static void udpNioServerOnce() throws Exception {
        int port = 6789;
        ByteBuffer buf = ByteBuffer.allocate(10);

        try (DatagramChannel dc = DatagramChannel.open()) {
            dc.bind(new InetSocketAddress(port));     // «слушаем» порт
            SocketAddress client = dc.receive(buf);   // блокирует, вернёт адрес отправителя

            buf.flip();
            while (buf.hasRemaining()) buf.put((byte) (buf.get() * 2));
            // Осторожно: позиция уже сдвинута; в реальном коде лучше
            // сделать flip() после обработки или работать через абсолютный доступ.

            buf.flip();
            dc.send(buf, client);                     // отправить обратно
        }
    }

    /** UDP-клиент на канале: connect(remote) + read/write. */
    static void udpNioClient() throws Exception {
        int port = 6789;
        SocketAddress addr = new InetSocketAddress("localhost", port);
        ByteBuffer buf = ByteBuffer.wrap(new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });

        try (DatagramChannel dc = DatagramChannel.open()) {
            dc.connect(addr);                         // для клиента удобно
            dc.write(buf);                            // = send(buf, addr)
            buf.clear();
            dc.read(buf);                             // = receive(buf)
            buf.flip();
            while (buf.hasRemaining()) System.out.print(buf.get() + " ");
            System.out.println();
        }

        // Отличие от DatagramSocket: работаем через ByteBuffer и Channel API.
        // send/receive — без соединения; write/read — после connect().
    }

    // --- TCP через ServerSocketChannel / SocketChannel ----------------------

    /** TCP-сервер на канале: ServerSocketChannel.bind + accept(). */
    static void tcpNioServerOnce() throws Exception {
        int port = 6789;
        ByteBuffer buf = ByteBuffer.allocate(10);

        try (ServerSocketChannel serv = ServerSocketChannel.open()) {
            serv.bind(new InetSocketAddress(port));
            try (SocketChannel sock = serv.accept()) { // блокирует, вернёт соединение
                sock.read(buf);                        // читаем из канала в буфер
                buf.flip();
                while (buf.hasRemaining()) buf.put((byte) (buf.get() * 2));

                buf.flip();
                sock.write(buf);                       // пишем обратно
            }
        }
    }

    /** TCP-клиент на канале: SocketChannel.open + connect(). */
    static void tcpNioClient() throws Exception {
        int port = 6789;
        SocketAddress addr = new InetSocketAddress("localhost", port);
        ByteBuffer buf = ByteBuffer.wrap(new byte[]{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });

        try (SocketChannel sock = SocketChannel.open()) {
            sock.connect(addr);
            sock.write(buf);

            buf.clear();
            sock.read(buf);
            buf.flip();
            while (buf.hasRemaining()) System.out.print(buf.get() + " ");
            System.out.println();
        }

        // Отличие от Socket/ServerSocket: работаем через ByteBuffer.
        // Каналы можно перевести в неблокирующий режим (configureBlocking(false))
        // и использовать с Selector — см. следующий блок.
    }
}
