
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Урок 1.6 — исключения и чистый код.
 */
public class App {

    private static final double VAT_RATE = 0.20;

    public static void main(String[] args) {
        part1();
        part2();
        part3();
        part4();
        part5();
        part6();
    }

    private static void part1() {
        System.out.println("1. try, catch и finally");

        try {
            int result = 10 / 0;
            System.out.println(result);
        } catch (ArithmeticException exception) {
            System.out.println("Нельзя делить на ноль");
        } finally {
            System.out.println("finally выполняется всегда");
        }

        try {
            Integer.parseInt("двадцать");
        } catch (NumberFormatException exception) {
            System.out.println("Строка не содержит целое число");
        }
    }

    private static void part2() {
        System.out.println("\n2. throws, throw и try-with-resources");

        Vegetable turnip = new Vegetable("репа");
        try (MyResource resource = new MyResource()) {
            resource.use();
            turnip.harvest(0);
        } catch (VeggyBreakException | IOException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Причина: " + exception.getCause());
        }
    }

    private static void part3() {
        System.out.println("\n3. Простой и читаемый код");

        double total = calculateOrderTotal(199.90, 3);
        System.out.printf("Итоговая стоимость: %.2f%n", total);
    }

    private static void part4() {
        System.out.println("\n4. Числа и переполнение");

        System.out.println("-3 нечётное: " + isOdd(-3));
        System.out.println("double: " + (2.00 - 1.10 == 0.90));

        BigDecimal price = new BigDecimal("2.00");
        BigDecimal discount = new BigDecimal("1.10");
        System.out.println("BigDecimal: " + price.subtract(discount));

        long wrong = 50_000 * 50_000;
        long correct = 50_000L * 50_000;
        System.out.println("Переполнение: " + wrong + "; корректно: " + correct);
    }

    private static void part5() {
        System.out.println("\n5. Литералы и приоритет операторов");

        System.out.println("Восьмеричный 01234: " + 01234);
        System.out.println("'H' + 'a': " + ('H' + 'a'));

        int value = 1_000;
        value *= 3 / 2;
        System.out.println("x *= 3 / 2: " + value);

        String text = null;
        String description = "text = " + (text != null ? text : "пусто");
        System.out.println(description);

        int lower = 0x01;
        int higher = 0x01;
        System.out.println("0x0101: " + ((higher << 8) + lower));
    }

    private static void part6() {
        System.out.println("\n6. Неочевидные случаи Java");

        BigInteger sum = BigInteger.ZERO;
        for (String five = "5"; !five.equals("500000"); five += "0") {
            sum = sum.add(new BigInteger(five));
        }
        System.out.println("BigInteger: " + sum);

        Integer first = 451;
        Integer second = 451;
        System.out.println("Integer ==: " + (first == second));
        System.out.println("Integer equals: " + first.equals(second));
        System.out.println("abs(MIN_VALUE): " + Math.abs(Integer.MIN_VALUE));
    }

    private static double calculateOrderTotal(double unitPrice, int quantity) {
        if (unitPrice < 0 || quantity < 0) {
            throw new IllegalArgumentException("Цена и количество не могут быть отрицательными");
        }

        double subtotal = unitPrice * quantity;
        return subtotal * (1 + VAT_RATE);
    }

    private static boolean isOdd(int value) {
        return value % 2 != 0;
    }

    private static class MyResource implements AutoCloseable {

        void use() throws IOException {
            System.out.println("Ресурс используется");
        }

        @Override
        public void close() {
            System.out.println("Ресурс закрыт");
        }
    }
}
