package itmo.practice.app;

import static java.lang.Math.PI;
import java.util.logging.Logger;

/**
 * Урок 1.7 — модули и инструменты разработки.
 */
public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        part1();
        part2();
        part3();
        part4();
        part5();
    }

    private static void part1() {
        System.out.println("1. Пакеты и import");

        Calculator calculator = new Calculator();
        int sum = calculator.add(7, 5);
        double circleArea = PI * 2 * 2;

        System.out.println("Пакет App: " + App.class.getPackageName());
        System.out.println("7 + 5 = " + sum);
        System.out.printf("Площадь круга: %.2f%n", circleArea);
    }

    private static void part2() {
        System.out.println("\n2. Модуль Java");

        Module module = App.class.getModule();
        System.out.println("Модуль: " + module.getName());
        System.out.println("Зависимости: " + module.getDescriptor().requires());
    }

    private static void part3() {
        System.out.println("\n3. Javadoc");

        Calculator calculator = new Calculator();
        System.out.println("Класс с документацией: " + calculator.getClass().getSimpleName());
        System.out.println("Команда: javadoc -d doc src/itmo/practice/app/*.java");
    }

    private static void part4() {
        System.out.println("\n4. Assert и проверка метода");

        Calculator calculator = new Calculator();
        assert calculator.add(2, 3) == 5 : "Сумма должна быть равна 5";

        try {
            calculator.divide(10, 0);
        } catch (IllegalArgumentException exception) {
            System.out.println("Негативный сценарий: " + exception.getMessage());
        }
    }

    private static void part5() {
        System.out.println("\n5. Логирование и отладка");

        LOGGER.info("Программа начала расчёт");

        Calculator calculator = new Calculator();
        int result = calculator.divide(20, 4);
        LOGGER.info(() -> "Результат расчёта: " + result);
        System.out.println("Поставь breakpoint на следующую строку в IDE.");
    }
}
