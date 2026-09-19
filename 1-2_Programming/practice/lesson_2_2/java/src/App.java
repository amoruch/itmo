import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Урок 2.2 — шаблоны проектирования и ввод-вывод.
 */
public class App {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        part1();
        part2();
        part3();
        part4();
        part5();
        part6();
    }

    private static void part1() {
        System.out.println("1. Iterator, Builder и сортировка");

        List<Task> tasks = new ArrayList<>(List.of(
                new Task("Черновик", 0),
                new Task("Прочитать лекцию", 1),
                new Task("Сделать лабораторную", 2)
        ));

        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().priority() == 0) {
                iterator.remove();
            }
        }

        tasks.sort(null);
        System.out.println("Comparable: " + tasks);
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task first, Task second) {
                return Integer.compare(first.priority(), second.priority());
            }
        });
        System.out.println("Comparator: " + tasks);

        Trip trip = new Trip.Builder()
                .where("Санкт-Петербург")
                .transport("поезд")
                .date(LocalDate.of(2026, 9, 1))
                .build();
        System.out.println("Builder: " + trip);
    }

    private static void part2() throws IOException {
        System.out.println("\n2. Байтные и символьные потоки");

        byte[] bytes;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            output.write("Java I/O".getBytes(StandardCharsets.UTF_8));
            bytes = output.toByteArray();
        }

        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            System.out.println("Байты: " + new String(input.readAllBytes(), StandardCharsets.UTF_8));
        }

        StringWriter characters = new StringWriter();
        try (PrintWriter writer = new PrintWriter(characters)) {
            writer.printf("%s %d", "Символов:", 7);
        }
        System.out.println(characters);
    }

    private static void part3() throws IOException {
        System.out.println("\n3. Декораторы потоков");

        byte[] bytes = "Первая строка\nВторая строка".getBytes(StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8)
        )) {
            System.out.println("BufferedReader: " + reader.readLine());
        }
    }

    private static void part4() throws IOException, ClassNotFoundException {
        System.out.println("\n4. Data streams и сериализация");

        byte[] numbers;
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             DataOutputStream output = new DataOutputStream(bytes)) {
            output.writeInt(42);
            output.writeUTF("Java");
            numbers = bytes.toByteArray();
        }
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(numbers))) {
            System.out.println("DataInputStream: " + input.readInt() + ", " + input.readUTF());
        }

        Student student = new Student("Аня", 3111);
        byte[] serialized;
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream output = new ObjectOutputStream(bytes)) {
            output.writeObject(student);
            serialized = bytes.toByteArray();
        }
        try (ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
            System.out.println("ObjectInputStream: " + input.readObject());
        }
    }

    private static void part5() {
        System.out.println("\n5. Scanner, стандартные потоки и Random");

        try (Scanner scanner = new Scanner("10 20\n30 40")) {
            int sum = 0;
            while (scanner.hasNextInt()) {
                sum += scanner.nextInt();
            }
            System.out.println("Scanner: сумма = " + sum);
        }

        Random random = new Random(7);
        System.out.println("Random: " + random.nextInt(100));
        System.out.println("Стандартный вывод: System.out; ошибки: System.err; ввод: System.in");
    }

    private static void part6() throws IOException {
        System.out.println("\n6. File, Path и Files");

        Path directory = Files.createTempDirectory("io-practice-");
        Path file = directory.resolve("notes.txt");
        try {
            Files.writeString(file, "Привет, NIO.2", StandardCharsets.UTF_8);
            String text = Files.readString(file, StandardCharsets.UTF_8);
            System.out.println("Path: " + file.getFileName() + "; текст: " + text);
            System.out.println("Файл существует: " + Files.exists(file));
        } finally {
            Files.deleteIfExists(file);
            Files.deleteIfExists(directory);
        }
    }

    private record Task(String name, int priority) implements Comparable<Task> {

        @Override
        public int compareTo(Task other) {
            return name.compareTo(other.name);
        }
    }

    private record Student(String name, int group) implements Serializable {
    }
}