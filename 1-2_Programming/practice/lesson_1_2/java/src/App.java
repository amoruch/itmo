
import java.util.Arrays;

/**
 * Урок 1.2 — инструкции управления.
 */
public class App {

    private static final int PASS_SCORE = 60;

    public static void main(String[] args) {
        printTitle("1. Ветвления и switch");
        part1();

        printTitle("2. Массивы");
        int[] scores = part2();

        printTitle("3. Циклы");
        part3(scores);

        printTitle("4. Строки и методы");
        part4(scores);
    }

    private static void part1() {
        int month = 2;
        int year = 2028;
        System.out.printf("В %d-%02d: %d дней%n", year, month, daysInMonth(month, year));
        System.out.println("Число -7: " + sign(-7));
        System.out.println("ReLU(-3) = " + relu(-3));

        // && не вычисляет правую часть, когда левая уже false.
        int divisor = 0;
        boolean canDivide = divisor != 0 && 10 / divisor > 1;
        System.out.println("Делить можно: " + canDivide);
    }

    private static int[] part2() {
        int[] scores = {48, 72, 0, 91, 65, 30, 88};
        System.out.println("Баллы: " + Arrays.toString(scores));

        int[] sameArray = scores;
        sameArray[0] = 55; // Копируется ссылка: изменение видно через обе переменные.
        int[] independentCopy = Arrays.copyOf(scores, scores.length);
        independentCopy[0] = 100;
        System.out.println("После изменения ссылки: " + Arrays.toString(scores));
        System.out.println("Независимая копия: " + Arrays.toString(independentCopy));

        int[][] groups = {{72, 91, 65}, {48, 0}, {88, 30, 76, 61}};
        printMatrix(groups);
        System.out.println("Первый результат от 90: " + findFirstScoreAtLeast(groups, 90));
        return scores;
    }

    private static void part3(int[] scores) {
        System.out.printf("Средний ненулевой балл: %.1f%n", averageNonZero(scores));
        System.out.println("До зачёта: " + scoreToPass(scores) + " баллов");
        countdown(3);

        int[] filtered = withoutMultiplesOfThreeOrFive(scores);
        System.out.println("Без кратных 3 и 5: " + Arrays.toString(filtered));
    }

    private static void part4(int[] scores) {
        String first = "Java";
        String second = new String("Java");
        System.out.println("first == second: " + (first == second));
        System.out.println("first.equals(second): " + first.equals(second));
        System.out.println(plural(24, "студент", "|а|ов"));
        System.out.println("sum(int): " + sum(2, 3));
        System.out.println("sum(double): " + sum(2.5, 3.5));
        greet("Аня", "Борис", "Вика");
        demonstrateArguments(scores);
    }

    private static int daysInMonth(int month, int year) {
        boolean leapYear = year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
        return switch (month) {
            case 2 ->
                leapYear ? 29 : 28;
            case 4, 6, 9, 11 ->
                30;
            case 1, 3, 5, 7, 8, 10, 12 ->
                31;
            default ->
                0;
        };
    }

    private static int sign(int value) {
        return value < 0 ? -1 : value > 0 ? 1 : 0;
    }

    private static int relu(int value) {
        return value < 0 ? 0 : value;
    }

    private static double averageNonZero(int[] values) {
        int sum = 0;
        int count = 0;
        for (int value : values) {
            if (value == 0) {
                continue;
            }
            sum += value;
            count++;
        }
        return count == 0 ? 0 : (double) sum / count;
    }

    private static int scoreToPass(int[] scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
            if (total >= PASS_SCORE) {
                break;
            }
        }
        return Math.max(0, PASS_SCORE - total);
    }

    private static void printMatrix(int[][] matrix) {
        System.out.println("Группы:");
        for (int[] group : matrix) {
            System.out.println("  " + Arrays.toString(group));
        }
    }

    private static int findFirstScoreAtLeast(int[][] matrix, int threshold) {
        for (int[] row : matrix) {
            for (int score : row) {
                if (score >= threshold) {
                    return score;
                }
            }
        }
        return -1;
    }

    private static void countdown(int from) {
        int value = from;
        while (value > 0) {
            System.out.print(value + " ");
            value--;
        }

        do {
            System.out.println("Старт!");
        } while (false); // do-while выполняет тело хотя бы один раз.
    }

    private static int[] withoutMultiplesOfThreeOrFive(int[] values) {
        int[] result = new int[values.length];
        int size = 0;
        for (int value : values) {
            if (value % 3 == 0 || value % 5 == 0) {
                continue;
            }
            result[size++] = value;
        }
        return Arrays.copyOf(result, size);
    }

    private static String plural(int number, String word, String endings) {
        String[] parts = endings.split("\\|");
        int lastTwoDigits = number % 100;
        int lastDigit = number % 10;
        int endingIndex = lastTwoDigits >= 11 && lastTwoDigits <= 14 ? 2
                : lastDigit == 1 ? 0 : lastDigit >= 2 && lastDigit <= 4 ? 1 : 2;
        return number + " " + word + parts[endingIndex];
    }

    private static int sum(int left, int right) {
        return left + right;
    }

    private static double sum(double left, double right) {
        return left + right;
    }

    private static void greet(String... names) {
        for (String name : names) {
            System.out.println("Привет, " + name + "!");
        }
    }

    private static void demonstrateArguments(int[] source) {
        int primitive = 10;
        increasePrimitive(primitive);
        increaseFirstElement(source);
        System.out.println("Примитив после метода: " + primitive);
        System.out.println("Массив после метода: " + Arrays.toString(source));
    }

    private static void increasePrimitive(int value) {
        value++;
    }

    private static void increaseFirstElement(int[] values) {
        values[0]++;
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }
}
