import java.util.Arrays;

/**
 * Урок 1.1 — введение.
 */
public class App {

    public static void main(String[] args) {
        // JDK = JRE + tools; JRE = JVM + API.
        // .java -javac-> .class (байт-код) -JVM-> выполнение. Кросс-платформенно.
        System.out.println("Привет, мир!");

        // Примитивные типы
        byte b = 127;
        short s = 32_767;
        int i = 2_147_483_647;
        long l = 9_223_372_036_854_775_807L;
        float f = 3.14f;
        double d = 3.14159;
        char c = 'A';
        boolean bool = true;

        // Ссылочные типы
        String str = "Java";
        int[] arr = {1, 2, 3};
        System.out.println(Arrays.toString(arr));

        // Литералы: 10, 017, 0xf4e0, 0b0101_1011, 2.998e25, '\t', "Java\u2122"
        int dec = 1_999_000, oct = 017, hex = 0xf4e0, bin = 0b0101_1011;

        // Преобразования: расширяющие неявно, сужающие явно
        long fromInt = 42;
        byte fromLong = (byte) 511; // -1
        System.out.println("(byte) 511 = " + fromLong);

        // Константы
        final int CONSTANT = 50;

        // Операторы
        int a = 10, bb = 3;
        System.out.println(a + bb);
        System.out.println(a / bb);       // целочисленное
        System.out.println(a % bb);

        int x = 5;
        int y = x++;                       // y=5, x=6
        int z = ++x;                       // x=7, z=7

        // Переполнение и особые значения
        System.out.println(Integer.MAX_VALUE + 1); // -2147483648
        System.out.println(1.0 / 0.0);              // Infinity
        System.out.println(0.0 / 0.0);              // NaN

        // Конкатенация строк
        System.out.println("hello" + 5 + 10);        // hello510
        System.out.println("hello" + (5 + 10));      // hello15
        System.out.println(10 + 5 + "Hello" + 5);    // 15Hello5

        // Math + формат
        System.out.println(Math.sqrt(16));
        System.out.printf("x = %4d, y = %02.1f%n", 42, 3.14159);

        // Именование: camelCase для переменных, SNAKE_CASE для констант
        final int HOURS_PER_DAY = 8, DAYS_PER_WEEK = 5;
        System.out.println("hours/week = " + HOURS_PER_DAY * DAYS_PER_WEEK);
    }
}
