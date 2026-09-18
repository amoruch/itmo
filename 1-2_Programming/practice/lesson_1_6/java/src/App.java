/**
 * Урок 1.6 — исключения, дизайн, чистый код.
 */
public class App {

    public static void main(String[] args) {
        // Иерархия: Throwable -> Error / Exception -> RuntimeException.
        // checked: Exception (кроме RuntimeException) — компилятор требует catch/throws.
        // unchecked: RuntimeException, Error — на усмотрение программиста.

        // try / catch / finally: finally выполняется всегда.
        try {
            int x = 1 / 0;
        } catch (ArithmeticException e) {
            System.out.println("division by zero");
        } finally {
            System.out.println("always");
        }

        // try-with-resources: закрывает AutoCloseable автоматически.
        try (var res = new MyResource()) {
            res.use();
        } catch (Exception e) {
            System.out.println("err: " + e.getMessage());
        }

        // Multi-catch: одно действие для разных исключений.
        try {
            if (args.length == 0) throw new java.io.IOException("no file");
        } catch (java.io.IOException | IllegalArgumentException e) {
            System.out.println("io or arg");
        }

        // Свои исключения: checked или unchecked (extends Exception / RuntimeException).
        try {
            harvest(0);
        } catch (VeggyBreakException e) {
            System.out.println("broken: " + e.getMessage()
                    + " cause=" + e.getCause());
        }

        // --- Принципы проектирования ---
        // DRY, KISS, YAGNI, SOLID, GRASP.
        // Наследование = жёстко (is-a); композиция = гибко (has-a).
        // Favor composition over inheritance.
        // Закон Деметры: не общайся с незнакомцами (одна точка на строку).

        // --- Чистый код ---
        // Осмысленные имена; методы короткие, с 0–2 аргументами.
        // Комментарии — только там, где код не может сказать сам.
        // Не возвращать/не принимать null там, где можно этого избежать.
        // Рефакторинг — регулярно, не ломая работающее.

        // --- Неочевидное в Java: ловушки ---
        // 1) isOdd: x % 2 == 1 неверно для отрицательных.
        System.out.println(isOdd(-3)); // false — а по идее true

        // 2) Деньги/дроби: double неточен.
        System.out.println(2.00 - 1.10 == 0.90); // false
        // 3) BigDecimal в конструкторе — только из String.
        System.out.println(new java.math.BigDecimal("2.00")
                .subtract(new java.math.BigDecimal("1.10")));

        // 4) Переполнение int до присваивания в long.
        long ms = 24 * 60 * 60 * 1000;      // int-умножение, но влезло
        long us = 24L * 60 * 60 * 1000 * 1000; // 24L — уже long
        System.out.println(ms / us);

        // 5) Octal-литерал: 01234 — не 1234.
        System.out.println(01234 + 54321);

        // 6) BigInteger неизменяем — нужен sum = sum.add(...).
        var sum = java.math.BigInteger.ZERO;
        for (var five = "5"; !five.equals("500000"); five += "0")
            sum = sum.add(new java.math.BigInteger(five));
        System.out.println(sum);

        // 7) char + char = int (а не строка).
        System.out.println('H' + 'a'); // 137

        // 8) x *= 3 / 2 — целочисленное деление внутри.
        var x = 1000;
        x *= 3 / 2;                 // x *= 1
        System.out.println(x);      // 1000

        // 9) Приоритет: конкатенация vs != в тернарном.
        String s = null;
        String r = "s = " + (s != null ? s : "0"); // скобки обязательны
        System.out.println(r);

        // 10) Сдвиг и + : + имеет больший приоритет.
        int lo = 0x01, hi = 0x01;
        System.out.println((hi << 8) + lo); // 0x0101

        // 11) Integer кэш [-128..127]: == работает только там.
        Integer a = 42, b = 42, c = 451, d = 451;
        System.out.println((a == b) + " " + (c == d));   // true false
        System.out.println(c.equals(d));                 // true

        // 11) Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE.
        System.out.println(Math.abs(Integer.MIN_VALUE));

        // 12) Забытый throw — исключение создано, но не выброшено.
        // new IllegalArgumentException("x"); // молча ничего не делает
    }

    static boolean isOdd(int x) { return x % 2 != 0; } // для отрицательных тоже

    static void harvest(double strength) {
        if (strength <= 0) {
            throw new VeggyBreakException("Репка разломилась!",
                    new IllegalStateException("strength=0"));
        }
    }

    /** Своё checked-исключение с цепочкой причин. */
    static class VeggyBreakException extends Exception {
        VeggyBreakException(String msg, Throwable cause) { super(msg, cause); }
    }

    /** AutoCloseable — можно использовать в try-with-resources. */
    static class MyResource implements AutoCloseable {
        void use() {}
        @Override public void close() { System.out.println("closed"); }
    }
}