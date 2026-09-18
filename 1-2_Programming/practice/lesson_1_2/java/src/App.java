import java.util.Arrays;

/**
 * Урок 1.2 — инструкции управления.
 */
public class App {

    public static void main(String[] args) {
        // Последовательность
        int x = 5, y = 3;
        System.out.println("x + y = " + (x + y));

        // Ветвление: if / else if / else
        int sign = x < 0 ? -1 : (x == 0 ? 0 : 1);
        System.out.println("sign = " + sign);

        // Сокращённые && / || не вычисляют правую часть при коротком замыкании
        int z = 0;
        if (z != 0 && y / z > 0) System.out.println("не выполнится");
        else System.out.println("&& защитил от деления на ноль");

        // switch-выражение (Java 14+)
        int month = 4;
        int days = switch (month) {
            case 2 -> 28;
            case 4, 6, 9, 11 -> 30;
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            default -> 0;
        };
        System.out.println("days = " + days);

        // Массивы: ссылочный тип, есть length
        int[] arr = {3, 1, 4, 1, 5};
        System.out.println("arr = " + Arrays.toString(arr));
        int[][] matrix = {{1, 2}, {3, 4, 5}}; // массив массивов, строки разной длины
        System.out.println("matrix[1].length = " + matrix[1].length);

        // Циклы: while, do-while, for, for-each
        int sum = 0;
        for (int v : arr) sum += v;
        System.out.println("sum = " + sum);

        int i = 0;
        while (i < 3) i++;
        do { i--; } while (i > 0);

        // break / continue
        int evenSum = 0;
        for (int v : arr) {
            if (v % 2 != 0) continue;
            evenSum += v;
        }
        System.out.println("evenSum = " + evenSum);

        // Строки: неизменяемы, сравнение через equals
        String s1 = "Hello";
        String s2 = new String("Hello");
        System.out.println("s1 == s2: " + (s1 == s2));         // false
        System.out.println("s1.equals(s2): " + s1.equals(s2)); // true

        // Методы: параметры по значению, varargs, перегрузка
        System.out.println(plural(24, "студент", "|a|ов"));
        System.out.println(sum(2, 3) + " / " + sum(2.5, 3.5));
        printAll("A", "B", "C");
    }

    /** Склонение существительного по числу. */
    static String plural(int num, String word, String decl) {
        int index = num / 10 % 10 == 1 || (num + 9) % 10 > 3
                ? 2 : num % 10 > 1 ? 1 : 0;
        return num + " " + word + decl.split("\\|")[index];
    }

    /** Перегрузка по типу параметров. */
    static int sum(int a, int b) { return a + b; }
    static double sum(double a, double b) { return a + b; }

    /** Переменное число параметров: String... == String[]. */
    static void printAll(String... names) {
        for (String n : names) System.out.print(n + " ");
        System.out.println();
    }
}
