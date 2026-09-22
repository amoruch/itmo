
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * Урок 2.4 — функции, Stream API, JDBC и безопасность.
 */
public class App {

    public static void main(String[] args) throws Exception {
        part1FunctionalInterfaces();
        part2Streams();
        part3SqlAndJdbc();
        part4RowSet();
        part5ServicesAndModules();
        part6Passwords();
    }

    /**
     * Функциональный интерфейс определяет форму лямбды.
     */
    static void part1FunctionalInterfaces() {
        Supplier<String> greeting = () -> "Привет";
        Consumer<String> print = System.out::println;
        Predicate<String> isLong = text -> text.length() >= 5;
        Function<String, String> decorate = String::toUpperCase;

        String result = decorate.andThen(text -> text + "!").apply(greeting.get());
        print.accept(result);
        System.out.println("Длинное слово: " + isLong.test("Java"));
    }

    /**
     * Stream — одноразовый ленивый конвейер: источник → операции → результат.
     */
    static void part2Streams() {
        List<Student> students = List.of(
                new Student("Аня", "P3115", 4.9),
                new Student("Борис", "P3115", 4.6),
                new Student("Вика", "P3116", 4.8)
        );

        Map<String, Double> averages = students.stream()
                .collect(Collectors.groupingBy(Student::group, Collectors.averagingDouble(Student::averageMark)));
        Optional<Student> best = students.stream().max(Comparator.comparingDouble(Student::averageMark));

        System.out.println("Средний балл: " + averages);
        System.out.println("Лучший студент: " + best.orElseThrow());
    }

    /**
     * SQL-значения передают параметрами, а не склеивают со строкой запроса.
     */
    static void part3SqlAndJdbc() {
        String query = "SELECT name, average_mark FROM students WHERE group_name = ?";
        System.out.println(query);
        System.out.println("? будет передан через PreparedStatement.setString(1, group).");
    }

    /**
     * CachedRowSet можно настроить отдельно от постоянного соединения с БД.
     */
    static void part4RowSet() throws SQLException {
        CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
        rowSet.setCommand("SELECT name FROM students WHERE group_name = ?");
        rowSet.setString(1, "P3115");
        System.out.println("RowSet command: " + rowSet.getCommand());
    }

    /**
     * ServiceLoader ищет реализации интерфейса, а модуль выражает зависимость
     * явно.
     */
    static void part5ServicesAndModules() {
        long driverProviders = ServiceLoader.load(Driver.class).stream().count();
        boolean sqlModulePresent = ModuleLayer.boot().findModule("java.sql").isPresent();

        System.out.println("Найдено JDBC-провайдеров: " + driverProviders);
        System.out.println("Модуль java.sql доступен: " + sqlModulePresent);
    }

    /**
     * Пароль превращают в медленный производный ключ с уникальной солью.
     */
    static void part6Passwords() throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        char[] password = "practice-password".toCharArray();

        try {
            byte[] hash = derivePasswordHash(password, salt);
            System.out.println("Хеш сохранён, длина: " + hash.length + " байт");
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    /**
     * Этот шаблон выполняют после получения реального Connection.
     */
    static void findStudents(Connection connection, String group) throws SQLException {
        String query = "SELECT name, average_mark FROM students WHERE group_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, group);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    System.out.println(result.getString("name") + ": " + result.getDouble("average_mark"));
                }
            }
        }
    }

    static byte[] derivePasswordHash(char[] password, byte[] salt) throws Exception {
        PBEKeySpec specification = new PBEKeySpec(password, salt, 100_000, 256);

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(specification)
                    .getEncoded();
        } finally {
            specification.clearPassword();
        }
    }

    record Student(String name, String group, double averageMark) {

    }
}
