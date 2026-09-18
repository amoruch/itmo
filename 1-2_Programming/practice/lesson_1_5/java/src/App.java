import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Урок 1.5 — полиморфизм и интерфейсы.
 */
public class App {

    public static void main(String[] args) {
        part1();
        part2();
        part3();
        part4();
        part5();
        part6();
        part7();
        part8();
        part9();
    }

    private static void part1() {
        System.out.println("1. Полиморфизм подтипов");

        Shape[] shapes = {
            new Rectangle(4, 3),
            new Circle(2)
        };

        for (Shape shape : shapes) {
            System.out.println(shape.description());
        }
    }

    private static void part2() {
        System.out.println("\n2. Абстрактный класс как общий тип");

        printShape(new Rectangle(2, 6));
        printShape(new Circle(1));

        // Shape shape = new Shape(); // Абстрактный класс нельзя создать через new.
    }

    private static void part3() {
        System.out.println("\n3. Интерфейсы и несколько аспектов поведения");

        Flyable[] flying = {new Duck(), new Airplane()};
        for (Flyable object : flying) {
            object.fly();
        }

        Duck duck = new Duck();
        duck.swim();
        duck.land();
        Flyable.rules();
    }

    private static void part4() {
        System.out.println("\n4. Comparable и Comparator");

        Task[] tasks = {
            new Task("Сделать лабораторную", 2),
            new Task("Прочитать лекцию", 1),
            new Task("Повторить Java", 3)
        };

        Arrays.sort(tasks);
        System.out.println("Естественный порядок: " + Arrays.toString(tasks));

        Arrays.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task first, Task second) {
                int priorityComparison = Integer.compare(first.priority(), second.priority());
                return priorityComparison != 0
                        ? priorityComparison
                        : first.name().compareTo(second.name());
            }
        });
        System.out.println("По приоритету: " + Arrays.toString(tasks));
    }

    private static void part5() {
        System.out.println("\n5. Вложенные и анонимные классы");

        Outer.Label label = new Outer.Label("static nested");
        Outer outer = new Outer(10);
        Outer.Counter counter = outer.new Counter();
        System.out.println(label.text());
        System.out.println("Поле Outer: " + counter.outerValue());
        System.out.println(outer.localClassMessage());

        Runnable greeting = new Runnable() {
            @Override
            public void run() {
                System.out.println("Привет из анонимного класса");
            }
        };
        greeting.run();
    }

    private static void part6() {
        System.out.println("\n6. ArrayList и List.of");

        ArrayList<String> subjects = new ArrayList<>();
        subjects.add("Java");
        subjects.add("Алгоритмы");
        subjects.add(1, "Дискретная математика");
        subjects.set(0, "Программирование");
        subjects.remove(2);
        System.out.println(subjects + ", размер: " + subjects.size());

        List<Integer> fixed = List.of(1, 2, 3);
        ArrayList<Integer> mutable = new ArrayList<>(fixed);
        mutable.add(4);
        System.out.println("Неизменяемый: " + fixed + "; изменяемый: " + mutable);
    }

    private static void part7() {
        System.out.println("\n7. Scanner");

        try (Scanner scanner = new Scanner("10 20\n30 40")) {
            int sum = 0;
            while (scanner.hasNextInt()) {
                sum += scanner.nextInt();
            }
            System.out.println("Сумма чисел: " + sum);
        }
    }

    private static void part8() {
        System.out.println("\n8. Дата и время");

        LocalDate start = LocalDate.of(2026, 3, 10);
        LocalDate deadline = LocalDate.parse("2026-05-01");
        LocalDate firstDay = start.with(TemporalAdjusters.firstDayOfMonth());
        LocalTime time = LocalTime.of(10, 30).plusMinutes(45);
        LocalDateTime meeting = LocalDateTime.of(start, time);
        ZonedDateTime moscowMeeting = meeting.atZone(ZoneId.of("Europe/Moscow"));

        System.out.println("Первый день месяца: " + firstDay);
        System.out.println("До дедлайна: " + Period.between(start, deadline));
        System.out.println("Встреча: " + moscowMeeting);
        System.out.println("Длительность пары: " + Duration.ofMinutes(95));
    }

    private static void part9() {
        System.out.println("\n9. Регулярные выражения");

        Pattern emailPattern = Pattern.compile("(\\w+)@(\\w+\\.)+\\w{2,}");
        Matcher matcher = emailPattern.matcher("student@se.itmo.ru");

        if (matcher.matches()) {
            System.out.println("Логин: " + matcher.group(1));
        }
    }

    private static void printShape(Shape shape) {
        System.out.println(shape.description());
    }
}