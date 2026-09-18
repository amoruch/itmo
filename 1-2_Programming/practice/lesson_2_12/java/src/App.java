import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * Урок 2.12 — NIO: Channels.
 */
public class App {

    public static void main(String[] args) throws Exception {
        channelBasics();
        fileChannelReadWrite();
        transferDemo();
        scatterGather();
        mapDemo();
    }

    /** Иерархия каналов и отличия от потоков. */
    static void channelBasics() {
        // java.nio.channels.Channel — базовый интерфейс:
        //   void close()
        //   boolean isOpen()
        //
        // Наследники:
        //   ReadableByteChannel  — int read(ByteBuffer)
        //   WritableByteChannel  — int write(ByteBuffer)
        //   ByteChannel          — оба
        //   SeekableByteChannel  — + position()
        //   ScatteringByteChannel  — long read(ByteBuffer[])
        //   GatheringByteChannel   — long write(ByteBuffer[])
        //   InterruptibleChannel   — можно прервать потоком
        //   NetworkChannel         — сетевые операции
        //
        // Отличия от потоков:
        //   - двунаправленный ввод-вывод;
        //   - неблокирующий режим;
        //   - асинхронный режим (AsynchronousChannel);
        //   - чтение/запись целого буфера (а не байта/массива).
    }

    /** FileChannel: чтение и запись через буфер. */
    static void fileChannelReadWrite() throws Exception {
        Path path = Files.createTempFile("nio-demo", ".bin");
        path.toFile().deleteOnExit();

        // Открытие с OpenOption:
        // READ, WRITE, APPEND, CREATE, CREATE_NEW, TRUNCATE_EXISTING, ...
        try (FileChannel ch = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.WRITE, StandardOpenOption.CREATE))) {
            ByteBuffer buf = ByteBuffer.allocate(64);
            buf.put("hello".getBytes());
            buf.flip();                    // limit = position, position = 0
            ch.write(buf);                 // запись из буфера в файл
        }

        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buf = ByteBuffer.allocate(64);
            ch.read(buf);                  // чтение из файла в буфер
            buf.flip();
            while (buf.hasRemaining()) System.out.print((char) buf.get());
            System.out.println();
        }

        // Мост из старого IO: FileInputStream/OutputStream.getChannel().
        // Мост из NIO: Files.newByteChannel(path, options).
    }

    /** transferTo / transferFrom — копирование канал-в-канал. */
    static void transferDemo() throws Exception {
        Path src = Files.createTempFile("nio-src", ".txt");
        Path dst = Files.createTempFile("nio-dst", ".txt");
        src.toFile().deleteOnExit();
        dst.toFile().deleteOnExit();
        Files.writeString(src, "some data");

        try (FileChannel in  = FileChannel.open(src, StandardOpenOption.READ);
             FileChannel out = FileChannel.open(dst, StandardOpenOption.WRITE)) {
            // Прямая передача из канала в канал без явного буфера.
            long bytes = in.transferTo(0, in.size(), out);
            // Или: out.transferFrom(in, 0, in.size());
            System.out.println("copied=" + bytes);
        }
    }

    /** Scattering (чтение в несколько буферов) и Gathering (запись из нескольких). */
    static void scatterGather() throws Exception {
        Path path = Files.createTempFile("nio-sg", ".txt");
        path.toFile().deleteOnExit();
        Files.writeString(path, "abcdefghij");

        // ScatteringByteChannel.read(ByteBuffer[]) — заполняет буферы по порядку.
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer[] parts = {
                    ByteBuffer.allocate(3),
                    ByteBuffer.allocate(4),
                    ByteBuffer.allocate(3)
            };
            ch.read(parts);
            for (ByteBuffer b : parts) {
                b.flip();
                while (b.hasRemaining()) System.out.print((char) b.get());
                System.out.print(" | ");
            }
            System.out.println();
        }

        // GatheringByteChannel.write(ByteBuffer[]) — собирает из нескольких буферов.
        // ch.write(parts);
    }

    /** Memory-mapped файлы: MappedByteBuffer. */
    static void mapDemo() throws Exception {
        Path path = Files.createTempFile("nio-map", ".bin");
        path.toFile().deleteOnExit();
        Files.write(path, new byte[1024]);

        try (FileChannel ch = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE))) {
            // map(mode, position, size) — отображение участка файла в память.
            // MapMode: READ_ONLY, READ_WRITE, PRIVATE.
            MappedByteBuffer map = ch.map(FileChannel.MapMode.READ_WRITE, 0, ch.size());
            System.out.println("isLoaded=" + map.isLoaded());
            map.put(0, (byte) 'H');       // работаем как с обычным ByteBuffer
            map.force();                  // сброс изменений на диск
            map.load();                   // подгрузить страницы в память
        }
    }
}
