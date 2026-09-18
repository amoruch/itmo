import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * Урок 2.5 — ввод-вывод: базовые потоки.
 */
public class App {

    public static void main(String[] args) throws Exception {
        // java.io   — потоки байтов/символов (классика, InputStream/Reader).
        // java.nio  — каналы и буферы (Channel/Buffer, позже — Path/Files).
        // Модель: источник → поток ввода → программа → поток вывода → приёмник.

        byteStreams();
        charStreams();
        fileStreams();
        memoryStreams();
        pipedStreams();
    }

    /** Байтовые потоки: 8 бит за единицу. */
    static void byteStreams() throws IOException {
        // InputStream  — abstract int read() → байт (0..255) или -1 (EOF).
        // OutputStream — abstract void write(int b).
        // Общие методы: read(byte[], off, len), write(byte[], off, len),
        //               available(), skip(n), close(), flush().
        // Байты ничего не знают о тексте — кодировку задаём отдельно.

        byte[] data = { 72, 101, 108, 108, 111 };           // "Hello" в ASCII
        try (InputStream in = new ByteArrayInputStream(data);
             OutputStream out = new ByteArrayOutputStream()) {
            int b;
            while ((b = in.read()) != -1) out.write(b);
            System.out.println(out);
        }

        // Кодировки: ASCII (7 бит), 8-битные (CP1251, KOI8-R), Unicode.
        // UTF-8 для 'Ё' = 3 байта, UTF-16 — 2 байта + BOM (FE FF или FF FE).
        // Java char = UTF-16 code unit, поэтому нужен переход байт ↔ символ.
    }

    /** Символьные потоки: единица — char (16 бит). */
    static void charStreams() throws IOException {
        // Reader — abstract int read(char[], off, len) / int read()
        // Writer — abstract void write(char[], off, len) / void write(int c)
        // Плюс Writer.append(int c), append(CharSequence).
        try (Reader in = new StringReader("Hello");
             Writer out = new StringWriter()) {
            int c;
            while ((c = in.read()) != -1) out.write(c);
            System.out.println(out);
        }

        // Мосты: InputStreamReader(InputStream, Charset) — байты → символы,
        //        OutputStreamWriter(OutputStream, Charset) — символы → байты.
    }

    /** Файловые потоки + try-with-resources. */
    static void fileStreams() throws IOException {
        var path = File.createTempFile("demo", ".bin");
        path.deleteOnExit();

        // try-with-resources закрывает всё, что implements AutoCloseable.
        try (FileOutputStream out = new FileOutputStream(path);
             FileInputStream  in  = new FileInputStream(path)) {
            for (int b : new byte[]{ 'H', 'i', '!' }) out.write(b);
            out.flush();
            int b;
            while ((b = in.read()) != -1) System.out.print((char) b);
            System.out.println();
        }

        var tpath = File.createTempFile("demo", ".txt");
        tpath.deleteOnExit();
        try (FileWriter out = new FileWriter(tpath);
             FileReader in  = new FileReader(tpath)) {
            out.write("Hello, Java!");
            out.flush();
            int c;
            while ((c = in.read()) != -1) System.out.print((char) c);
            System.out.println();
        }

        // Closeable  — close() throws IOException (освобождение ресурса).
        // Flushable  — flush() сливает буфер в приёмник.
        // AutoCloseable — Closable extends AutoCloseable, for try-with-resources.
    }

    /** Специализированные потоки в памяти: массив / строка. */
    static void memoryStreams() throws IOException {
        var bout = new ByteArrayOutputStream();
        bout.write("bytes".getBytes(StandardCharsets.UTF_8));
        byte[] bytes = bout.toByteArray();

        var cin = new CharArrayReader("abc".toCharArray());
        var cout = new CharArrayWriter();
        int c; while ((c = cin.read()) != -1) cout.write(c);
        char[] chars = cout.toCharArray();

        var sw = new StringWriter();
        sw.write("built from chars");
        String s = sw.toString();

        System.out.println(new String(bytes, StandardCharsets.UTF_8)
                + " / " + new String(chars) + " / " + s);
    }

    /** Каналы (pipe) — соединение потоков внутри процесса. */
    static void pipedStreams() throws IOException {
        try (var out = new PipedWriter();
             var in  = new PipedReader(out)) {
            out.write("ping");
            int c; while ((c = in.read()) != -1) System.out.print((char) c);
            System.out.println();
        }
        // Для байтов: PipedOutputStream + PipedInputStream (нужен connect).
    }
}
