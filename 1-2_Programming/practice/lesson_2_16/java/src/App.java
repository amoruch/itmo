import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Урок 2.16 — блокирующий / неблокирующий режим и Selector.
 */
public class App {

    public static void main(String[] args) throws Exception {
        blockingVsNonBlocking();
        selectorAnatomy();
        selectorLoop();
    }

    /** Два режима работы каналов. */
    static void blockingVsNonBlocking() throws Exception {
        // Блокирующий (по умолчанию):
        //   - операция блокирует поток до завершения;
        //   - пока не закончится — продолжить нельзя.
        //
        // Неблокирующий:
        //   - read()/write()/accept() возвращают 0 или null, если данных/события нет;
        //   - можно заняться другими делами и попробовать позже.

        // Переключение:
        //   channel.configureBlocking(false);
        //
        // Поведение в неблокирующем режиме:
        //   SocketChannel.read → int (число прочитанных байт) или 0;
        //   SocketChannel.write → int (сколько удалось записать) или 0;
        //   ServerSocketChannel.accept → SocketChannel или null.

        // Таймаут для блокирующего режима:
        //   Socket.setSoTimeout(ms), ServerSocket.setSoTimeout(ms),
        //   DatagramSocket.setSoTimeout(ms) — бросят SocketTimeoutException.

        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.configureBlocking(false);           // с этого момента accept() не блокирует
            ssc.bind(new InetSocketAddress(0));
            SocketChannel sc = ssc.accept();        // null, если никто не подключился
            System.out.println("accepted=" + sc);
        }
    }

    /** Основные сущности Selector. */
    static void selectorAnatomy() throws Exception {
        // Selector — мультиплексор: один поток обслуживает много каналов.
        try (Selector selector = Selector.open()) {
            // Зарегистрировать канал:
            //   channel.register(selector, ops, attachment)
            // ops — битовая маска событий:
            //   OP_ACCEPT  — новое входящее соединение (ServerSocketChannel)
            //   OP_CONNECT — соединение установлено (SocketChannel)
            //   OP_READ    — есть данные для чтения
            //   OP_WRITE   — буфер готов принять данные

            // Состояния:
            //   keys()           — все зарегистрированные ключи;
            //   selectedKeys()   — ключи, по которым произошли события;
            //   cancelledKeys()  — отменённые.
        }
    }

    /** Схема сервера на Selector: accept → read → write. */
    static void selectorLoop() throws Exception {
        // Мини-сервер: принимает соединения, читает байты, тут же отвечает эхом.
        try (Selector selector = Selector.open();
             ServerSocketChannel server = ServerSocketChannel.open()) {

            server.configureBlocking(false);
            server.bind(new InetSocketAddress(6789));
            server.register(selector, SelectionKey.OP_ACCEPT);

            long stopAt = System.currentTimeMillis() + 2_000; // ограничим демо 2 с

            while (System.currentTimeMillis() < stopAt) {
                // select() блокирует поток, пока не будет хотя бы одного события.
                // select(timeout) — вернётся не позже чем через timeout мс.
                if (selector.select(200) == 0) continue;

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();                         // обязательно: иначе сработает снова

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        // OP_ACCEPT: новое соединение.
                        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ,
                                ByteBuffer.allocate(1024)); // attachment — буфер клиента
                    } else if (key.isReadable()) {
                        // OP_READ: есть данные. Читаем в прикреплённый буфер.
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buf = (ByteBuffer) key.attachment();
                        int n = client.read(buf);
                        if (n == -1) {                   // клиент закрыл соединение
                            key.cancel();
                            client.close();
                        } else if (n > 0) {
                            // Переключаемся на запись.
                            buf.flip();
                            client.register(selector, SelectionKey.OP_WRITE, buf);
                        }
                    } else if (key.isWritable()) {
                        // OP_WRITE: буфер готов — отправляем ответ.
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buf = (ByteBuffer) key.attachment();
                        client.write(buf);
                        if (!buf.hasRemaining()) {
                            key.cancel();
                            client.close();
                        }
                    }
                }
            }
        }
    }
}
