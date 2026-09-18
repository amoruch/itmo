import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Урок 2.6 — декораторы ввода-вывода.
 */
public class App {

    public static void main(String[] args) throws Exception {
        filterBase();
        buffered();
        lineNumbers();
        bridges();
        print();
        data();
        compression();
    }

    /** Базовые декораторы: Filter* — исходный поток передаётся в конструктор. */
    static void filterBase() {
        // FilterInputStream(InputStream), FilterOutputStream(OutputStream),
        // FilterReader(Reader), FilterWriter(Writer).
        // Все остальные декораторы наследуются от них.
        // Идея: оборачиваем поток и добавляем функциональность,
        // не меняя интерфейс самого потока (паттерн Decorator).
    }

    /** Buffered* — буфер для производительности + построчная работа. */
    static void buffered() throws IOException {
        try (var out = new BufferedWriter(new StringWriter());
             var in  = new BufferedReader(new StringReader("line1\nline2\n"))) {
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
        }
        // BufferedInputStream / BufferedOutputStream — то же для байтов.
    }

    /** LineNumberReader — номера строк. */
    static void lineNumbers() throws IOException {
        try (var in = new LineNumberReader(new StringReader("a\nb\nc\n"))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(in.getLineNumber() + ": " + line);
            }
            in.setLineNumber(100); // меняет только счётчик, не поток
        }
    }

    /** Мосты байты ↔ символы: кодировка задаётся явно. */
    static void bridges() throws IOException {
        var bytes = "Привет".getBytes(StandardCharsets.UTF_8);
        try (var in  = new InputStreamReader(new ByteArrayInputStream(bytes),
                                             StandardCharsets.UTF_8);
             var out = new OutputStreamWriter(new ByteArrayOutputStream(),
                                             StandardCharsets.UTF_8)) {
            int c;
            while ((c = in.read()) != -1) out.write(c);
        }
        // Без указания Charset используется platform default — не переносимо.
    }

    /** PrintStream / PrintWriter — print / println / printf / format. */
    static void print() {
        // System.out и System.err — это PrintStream.
        // PrintWriter используется для символьных приёмников.
        var out = new PrintWriter(System.out, true);
        out.printf("%c = %2$+9.7f%n", 'π', Math.PI);
        // Формат: %[индекс$][флаги][размер][.точность]спецификатор
        //   %b boolean, %h hashcode, %d decimal, %f float, %t дата,
        //   %n перевод строки, %% сам символ %.
    }

    /** Data* — примитивы и строки ↔ байты. Порядок чтения = порядок записи. */
    static void data() throws IOException {
        var bout = new ByteArrayOutputStream();
        try (var out = new DataOutputStream(bout)) {
            out.writeInt(42);
            out.writeDouble(3.14);
            out.writeUTF("hi");
        }
        try (var in = new DataInputStream(new ByteArrayInputStream(bout.toByteArray()))) {
            System.out.println(in.readInt() + " " + in.readDouble() + " " + in.readUTF());
        }
        // Методы: byte, short, int, long, float, double, char, boolean, UTF.
    }

    /** Сжатие: Deflater* / Inflater* и GZIP / ZIP. */
    static void compression() throws IOException {
        var raw = "some text to compress".getBytes(StandardCharsets.UTF_8);
        var compressed = new ByteArrayOutputStream();
        try (var gz = new GZIPOutputStream(compressed)) {
            gz.write(raw);
        }
        try (var in = new GZIPInputStream(new ByteArrayInputStream(compressed.toByteArray()))) {
            System.out.println(new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
        // Семейство: Deflater/Inflater → GZIP → ZIP → JAR.
    }
}
