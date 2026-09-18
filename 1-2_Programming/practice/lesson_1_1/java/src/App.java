
import java.util.Arrays;

/**
 * Лекция 1: Java-платформа, типы данных и выражения.
 */
public class App {

    // these consts are work time
    private static final int HOURS_PER_DAY = 8;
    private static final int DAYS_PER_WEEK = 5;
    private static final int WEEKS_PER_MONTH = 4;

    public static void main(String[] args) {
        printTitle("1. Java и точка входа");
        part1();

        printTitle("2. Переменные, литералы и типы");
        part2();

        printTitle("3. Преобразования и выражения");
        part3();

        printTitle("4. Math, строки и форматированный вывод");
        part4();
    }

    private static void part1() {
        // javac превращает App.java в байт-код App.class, JVM исполняет байт-код.
        System.out.println("Hello, Java!");
        System.out.println("Версия JVM: " + System.getProperty("java.version"));
        System.out.println("ОС: " + System.getProperty("os.name"));
    }

    private static void part2() {
        // Примитивы хранят значение, String и массив — ссылочные типы.
        byte completedLabs = 3;
        short groupNumber = 3111;
        int students = 28;
        long population = 8_100_000_000L;
        float temperature = 22.5F;
        double averageScore = 87.35;
        char groupLetter = 'P';
        boolean isDeadlineNear = false;

        String course = "Программирование";
        int[] scores = {78, 91, 64, 88};

        // Литералы можно записывать в разных системах счисления и разделять _.
        int decimal = 1_999_000;
        int binary = 0b0101_1011;
        int hexadecimal = 0xF4E0;

        System.out.printf("%s, группа %c-%d: %d студентов, %d лаб сдано%n",
                course, groupLetter, groupNumber, students, completedLabs);
        System.out.printf("Население: %d, t = %.1f, средний балл = %.2f, дедлайн: %b%n",
                population, temperature, averageScore, isDeadlineNear);
        System.out.println("Баллы: " + Arrays.toString(scores));
        System.out.printf("Литералы: %d, %d, %d%n", decimal, binary, hexadecimal);
    }

    private static void part3() {
        int source = 511;
        long widened = source;           // Расширение int -> long происходит неявно.
        byte narrowed = (byte) source;   // Сужение требует явного приведения: получится -1.

        int wholeDivision = 15 / 4;
        double decimalDivision = 15 / 4.0;
        int remainder = 15 % 4;

        int counter = 5;
        int postIncrement = counter++;   // Сначала используем 5, потом counter станет 6.
        int preIncrement = ++counter;    // Сначала counter станет 7, затем используем 7.

        int overflow = Integer.MAX_VALUE + 1;
        double infinity = 1.0 / 0.0;
        double notANumber = 0.0 / 0.0;

        System.out.printf("long = %d, (byte) %d = %d%n", widened, source, narrowed);
        System.out.printf("15 / 4 = %d; 15 / 4.0 = %.2f; остаток = %d%n",
                wholeDivision, decimalDivision, remainder);
        System.out.printf("post = %d, pre = %d, counter = %d%n",
                postIncrement, preIncrement, counter);
        System.out.printf("Переполнение int: %d; Infinity: %s; NaN: %s%n",
                overflow, infinity, notANumber);
    }

    private static void part4() {
        int workHoursPerMonth = HOURS_PER_DAY * DAYS_PER_WEEK * WEEKS_PER_MONTH;
        double radius = 2.5;
        double circleArea = Math.PI * Math.pow(radius, 2);
        int rounded = (int) Math.round(circleArea);

        // Скобки управляют приоритетом + при конкатенации со строками.
        String withoutBrackets = "Сумма: " + 5 + 10;
        String withBrackets = "Сумма: " + (5 + 10);

        System.out.printf("Рабочих часов за месяц: %d%n", workHoursPerMonth);
        System.out.printf("Круг радиуса %.1f: площадь = %.3f, округлённо = %d%n",
                radius, circleArea, rounded);
        System.out.println(withoutBrackets);
        System.out.println(withBrackets);
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }
}
