
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

/**
 * Урок 2.3 — NIO, сеть, дата-время и лямбды.
 */
public class App {

    public static void main(String[] args) throws Exception {
        part1Buffers();
        part2Channels();
        part3NetworkAddresses();
        part4Selector();
        part5UriAndUrl();
        part6DateAndTime();
        part7Lambdas();
    }

    /**
     * Буфер: запись → flip() → чтение.
     */
    static void part1Buffers() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putInt(2026);
        buffer.put("Java".getBytes(UTF_8));
        System.out.println("before flip: position=" + buffer.position() + ", limit=" + buffer.limit());

        buffer.flip();
        int year = buffer.getInt();
        byte[] letters = new byte[buffer.remaining()];
        buffer.get(letters);
        System.out.println(year + " " + new String(letters, UTF_8));

        buffer.clear();
        System.out.println("after clear: position=" + buffer.position() + ", limit=" + buffer.limit());
    }

    /**
     * FileChannel читает и пишет байты через ByteBuffer.
     */
    static void part2Channels() throws IOException {
        Path file = Files.createTempFile("nio-practice-", ".txt");

        try (FileChannel channel = FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            channel.write(UTF_8.encode("channel"));
            channel.position(0);

            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);
            buffer.flip();
            System.out.println(UTF_8.decode(buffer));
        } finally {
            Files.deleteIfExists(file);
        }
    }

    /**
     * Адрес состоит из IP (или имени хоста) и порта.
     */
    static void part3NetworkAddresses() throws IOException {
        InetAddress loopback = InetAddress.getLoopbackAddress();
        InetSocketAddress endpoint = new InetSocketAddress(loopback, 8080);
        byte[] data = "ping".getBytes(UTF_8);
        DatagramPacket packet = new DatagramPacket(data, data.length, endpoint);

        System.out.println(packet.getAddress().getHostAddress() + ":" + packet.getPort());
        System.out.println("TCP передаёт поток данных, UDP — независимые датаграммы.");
    }

    /**
     * Selector позволяет одному потоку следить за несколькими неблокирующими
     * каналами.
     */
    static void part4Selector() throws IOException {
        try (Selector selector = Selector.open(); ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0));
            server.configureBlocking(false);
            SelectionKey key = server.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("selector key: accept=" + key.isAcceptable() + ", valid=" + key.isValid());
        }
    }

    /**
     * URI разбирает адрес, URL умеет создавать URLConnection.
     */
    static void part5UriAndUrl() throws Exception {
        URI uri = new URI("https://example.com:443/api?course=java#nio");
        URL url = uri.toURL();

        System.out.println(uri.getScheme() + ": host=" + uri.getHost() + ", path=" + uri.getPath());
        System.out.println("URL protocol=" + url.getProtocol() + ", default port=" + url.getDefaultPort());
    }

    /**
     * Старый Date можно перевести в современный неизменяемый java.time.
     */
    static void part6DateAndTime() {
        Instant moment = Instant.now();
        Date legacyDate = Date.from(moment);
        ZonedDateTime moscowTime = moment.atZone(ZoneId.of("Europe/Moscow"));
        LocalDate deadline = LocalDate.of(2026, 9, 30);

        System.out.println("legacy → instant: " + legacyDate.toInstant());
        System.out.println(moscowTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        ZonedDateTime deadlineStart = deadline.atStartOfDay(ZoneId.of("Europe/Moscow"));
        System.out.println("until deadline: " + Duration.between(moment, deadlineStart));
    }

    /**
     * Predicate и Consumer делают отбор и обработку универсальными.
     */
    static void part7Lambdas() {
        List<Student> students = List.of(
                new Student("Аня", "P3115", 19, 4.9),
                new Student("Борис", "P3115", 21, 4.8),
                new Student("Вика", "P3116", 18, 4.6)
        );

        handle(students, student -> student.age() < 20 && student.averageMark() > 4.75, System.out::println);

        int factor = 2; // effectively final: его можно захватить лямбдой.
        IntUnaryOperator doubleValue = value -> value * factor;
        System.out.println("7 * 2 = " + doubleValue.applyAsInt(7));
        System.out.println("factorial(5) = " + factorial(5));
    }

    static <T> void handle(Iterable<T> values, Predicate<? super T> filter, Consumer<? super T> action) {
        for (T value : values) {
            if (filter.test(value)) {
                action.accept(value);
            }
        }
    }

    static int factorial(int value) {
        return value <= 1 ? 1 : value * factorial(value - 1);
    }

    record Student(String name, String group, int age, double averageMark) {

    }
}
