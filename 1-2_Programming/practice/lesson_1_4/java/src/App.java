
import java.math.BigDecimal;

/**
 * Урок 1.4 — наследование.
 */
public class App {

    public static void main(String[] args) {
        part1();
        part2();
        part3();
        part4();
    }

    private static void part1() {
        System.out.println("1. Наследование и полиморфизм");

        Shape[] shapes = {
            new Rectangle(4, 3),
            new Square(5),
            new Circle(2)
        };

        for (Shape shape : shapes) {
            System.out.printf("%s: площадь = %.2f, периметр = %.2f%n",
                    shape.description(), shape.area(), shape.perimeter());
        }
    }

    private static void part2() {
        System.out.println("\n2. super, @Override и приведение типов");

        Square square = new Square(4);
        System.out.println(square);

        Shape shape = new ColoredSquare(3, "синий"); // upcasting
        System.out.println(shape.description());

        if (shape instanceof ColoredSquare coloredSquare) {
            System.out.println("Цвет: " + coloredSquare.color()); // downcasting после проверки
        }
    }

    private static void part3() {
        System.out.println("\n3. Object, enum и record");

        Rectangle first = new Rectangle(2, 6);
        Rectangle second = new Rectangle(2, 6);
        System.out.println(first);
        System.out.println("equals: " + first.equals(second));
        System.out.println("hashCode одинаковые: " + (first.hashCode() == second.hashCode()));

        Season season = Season.SUMMER;
        Point point = new Point(3, 7);
        System.out.println(season + ": " + season.description());
        System.out.println(point + ", x = " + point.x());
    }

    private static void part4() {
        System.out.println("\n4. Строки, обёртки и точные числа");

        StringBuilder message = new StringBuilder("Java");
        message.append(" изучает ").append(12).append(" студентов");
        System.out.println(message);

        Integer first = 128;
        Integer second = 128;
        System.out.println("Integer ==: " + (first == second));
        System.out.println("Integer equals: " + first.equals(second));

        BigDecimal price = new BigDecimal("19.90");
        BigDecimal count = new BigDecimal("3");
        System.out.println("Стоимость: " + price.multiply(count));
    }
}
