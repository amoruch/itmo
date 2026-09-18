import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Урок 2.10 — файлы: java.io.File vs java.nio.file (NIO.2).
 */
public class App {

    public static void main(String[] args) throws Exception {
        fileVsPath();
        pathDemo();
        filesChecks();
        filesCreateDelete();
        filesReadWrite();
        openOptionsDemo();
        walkDemo();
    }

    // --- java.io.File vs java.nio.file.Path ------------------------------

    static void fileVsPath() {
        // java.io.File   — старый API (Java 1.0). Всё, что связано с путями и файлами.
        //   - методы редко бросают исключения → тихие ошибки;
        //   - нет символических ссылок, атрибутов, альтернативных FS.
        // java.nio.file.Path — новый API (Java 7, NIO.2).
        //   - методы бросают IOException → явная обработка;
        //   - симлинки, расширенные атрибуты, альтернативные FS;
        //   - настраиваемый разделитель, кросс-платформенность.
        // Мост между ними:
        //   Path p = file.toPath();
        //   File f = path.toFile();

        // Файловая система:
        //   Windows (NTFS) — раздельные FS, разделитель '\', C:\Users\student\file.txt
        //   Unix / MacOS   — виртуальная FS, разделитель '/', /home/student/file.txt
    }

    // --- Path / Paths / FileSystems --------------------------------------

    static void pathDemo() {
        // Абсолютный / относительный / пустой (= текущая директория).
        Path p1 = Path.of("java", "Hello.java");       // Java 11+, рекомендуется
        Path p2 = Paths.get("/home/student/file.txt"); // старый стиль
        Path abs = p1.toAbsolutePath();
        Path norm = p1.normalize();

        System.out.println(p1 + " / " + abs + " / " + norm);

        // FileSystem / FileSystems — работа с ФС.
        FileSystem fs = FileSystems.getDefault();
        System.out.println("separator=" + fs.getSeparator());
        for (Path root : fs.getRootDirectories()) System.out.println("root=" + root);

        // Части пути:
        //   getFileName(), getParent(), getRoot(),
        //   resolve(other) — достроить, relativize(other) — относительный путь,
        //   startsWith / endsWith, toUri().
    }

    // --- Files: проверки --------------------------------------------------

    static void filesChecks() {
        Path p = Path.of(".");
        System.out.println(Files.exists(p));      // false — если нет
        System.out.println(Files.notExists(p));   // true  — если нет
        System.out.println(Files.isReadable(p));
        System.out.println(Files.isWritable(p));
        System.out.println(Files.isExecutable(p));
        // Флаги для exists/notExists:
        //   LinkOption.NOFOLLOW_LINKS — не разыменовывать символическую ссылку.
    }

    // --- Files: создание и удаление --------------------------------------

    static void filesCreateDelete() throws IOException {
        Path tmp = Files.createTempFile("demo", ".txt");
        System.out.println("created: " + tmp);

        Path file = Files.createFile(Path.of(tmp.getParent().toString(), "demo2.txt"));

        Files.delete(file);                       // бросит, если файла нет
        boolean removed = Files.deleteIfExists(file); // false, если нет
        System.out.println("deleted=" + removed);

        // Создание директорий:
        //   Files.createDirectory(path)     — одна (родитель должен существовать)
        //   Files.createDirectories(path)   — вложенные, если нет.
    }

    // --- Files: чтение и запись целиком ----------------------------------

    static void filesReadWrite() throws IOException {
        Path p = Files.createTempFile("data", ".txt");

        // Байты:
        byte[] bytes = "hello".getBytes(StandardCharsets.UTF_8);
        Files.write(p, bytes);
        byte[] back = Files.readAllBytes(p);

        // Строки:
        List<String> lines = List.of("Hello all!", "Bye all!");
        Files.write(p, lines);
        List<String> read = Files.readAllLines(p);

        // Строки целиком (Java 11+):
        Files.writeString(p, "Hello all!");
        String content = Files.readString(p);

        System.out.println(content + " / " + read.size() + " / " + back.length);

        // Потоки (аналог File*/Buffered*):
        //   Files.newInputStream(path, OpenOption...)
        //   Files.newOutputStream(path, OpenOption...)
        //   Files.newBufferedReader(path, Charset, OpenOption...)
        //   Files.newBufferedWriter(path, Charset, OpenOption...)
    }

    // --- OpenOption: как открывать файлы ---------------------------------

    static void openOptionsDemo() throws IOException {
        // StandardOpenOption — флаги для newBufferedWriter / newOutputStream:
        //   CREATE              — создать, если нет
        //   CREATE_NEW          — создать, ошибка если уже есть
        //   WRITE               — открыть на запись
        //   READ                — открыть на чтение
        //   APPEND              — писать в конец
        //   TRUNCATE_EXISTING   — очистить (по умолчанию при WRITE)
        //   DELETE_ON_CLOSE     — удалить при закрытии
        //   SPARSE              — разреженный файл
        //   SYNC / DSYNC        — синхронизировать с диском
        Path log = Files.createTempFile("log", ".txt");
        try (var w = Files.newBufferedWriter(log, StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            w.write("appended line\n");
        }
    }

    // --- Обход дерева ----------------------------------------------------

    static void walkDemo() throws IOException {
        Path root = Path.of(".");
        // Files.walk — рекурсивный обход (поток путей).
        try (var stream = Files.walk(root, 2)) {
            stream.filter(Files::isRegularFile)
                  .limit(5)
                  .forEach(System.out::println);
        }

        // Files.walkFileTree — событийный обход (preVisitDirectory,
        // visitFile, postVisitDirectory, visitFileFailed) с FileVisitor.
        // Удобно для удаления/копирования целых деревьев.
    }
}
