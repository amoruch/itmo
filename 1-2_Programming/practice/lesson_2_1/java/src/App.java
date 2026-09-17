/**
 * Урок 2.1 — типы данных, классы-обёртки и преобразования.
 */
public class App {

    public static void main(String[] args) {
        // Java — статически и строго типизированный язык.
        int students = 10;
        String group = "P3111";
        // students = group; // Ошибка компиляции: int и String — разные типы.

        // Примитивные типы: byte, short, int, long, float, double, char, boolean.
        byte small = 100;
        long population = 8_100_000_000L; // суффикс L нужен для long-литерала
        float temperature = 23.5F;        // суффикс F нужен для float-литерала
        double pi = 3.14159;
        boolean isOpen = true;

        System.out.printf(
                "%s: %d студентов, small = %d, население Земли ≈ %d, t = %.1f, π = %.5f, открыто: %b%n",
                group, students, small, population, temperature, pi, isOpen);

        // У каждого примитива есть класс-обёртка: Integer, Double, Boolean и т. д.
        Integer boxed = 42; // автоупаковка: int -> Integer
        int unboxed = boxed; // автораспаковка: Integer -> int
        double asDouble = boxed.doubleValue();
        System.out.printf("Integer: %d, int: %d, double: %.1f%n", boxed, unboxed, asDouble);

        // Преобразования могут быть неявными (без потери данных) и явными.
        int whole = 300;
        long widened = whole;          // int -> long: неявно
        byte narrowed = (byte) whole;  // int -> byte: явно; результат переполнится
        System.out.printf("long: %d, byte после сужения: %d%n", widened, narrowed);

        // Строку можно преобразовать в число методами классов-обёрток.
        int year = Integer.parseInt("2026");
        boolean ready = Boolean.parseBoolean("true");
        System.out.printf("Год: %d; готово: %b%n", year, ready);

        // char хранит одну UTF-16 кодовую единицу.
        // Символы вне BMP (например, 😀) занимают пару char.
        char highSurrogate = '\uD83D';
        char lowSurrogate = '\uDE00';
        int codePoint = Character.toCodePoint(highSurrogate, lowSurrogate);
        System.out.printf("Кодовая точка: U+%X, символ: %s%n",
                codePoint, new String(Character.toChars(codePoint)));
    }
}
