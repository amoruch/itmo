
/**
 * Урок 1.3 — основы ООП.
 */
public class App {

    public static void main(String[] args) {
        printTitle("1. Объекты, поля и методы");
        part1();

        printTitle("2. Инкапсуляция и конструкторы");
        part2();

        printTitle("3. Композиция и static");
        part3();

        printTitle("4. Неизменяемые объекты");
        part4();
    }

    private static void part1() {
        Rectangle[] rectangles = {
            new Rectangle(3, 4),
            new Rectangle(5.5, 2)
        };

        for (Rectangle rectangle : rectangles) {
            System.out.printf("%s, площадь: %.2f%n", rectangle.description(), rectangle.area());
        }

        Rectangle first = rectangles[0];
        Rectangle sameObject = first;
        sameObject.setWidth(10);
        System.out.println("Ширина first после изменения ссылки: " + first.getWidth());
        // Rectangle absent = null;
        // absent.area(); // NullPointerException
    }

    private static void part2() {
        Rectangle unit = new Rectangle(); // Конструктор без параметров вызывает this(1, 1).
        System.out.println("Единичный: " + unit.description());

        try {
            unit.setSides(-1, 5);
        } catch (IllegalArgumentException exception) {
            System.out.println("Некорректные стороны: " + exception.getMessage());
        }

        unit.setSides(6, 2);
        System.out.printf("После setSides: %s, периметр: %.2f%n",
                unit.description(), unit.perimeter());
    }

    private static void part3() {
        Point center = new Point(2, 3);
        Circle circle = new Circle(center, 4);
        System.out.printf("%s, площадь: %.2f%n", circle.description(), circle.area());
        System.out.println("Сторон у прямоугольника: " + Rectangle.NUMBER_OF_SIDES);

        Student first = new Student("Аня");
        Student second = new Student("Борис");
        System.out.println(first.description());
        System.out.println(second.description());
    }

    private static void part4() {
        ImmutableRectangle rectangle = new ImmutableRectangle(8, 3, new Point(0, 0));
        Point origin = rectangle.getOrigin();
        System.out.printf("%s, площадь: %.2f%n", rectangle.description(), rectangle.area());
        System.out.println("Координаты начала: " + origin.getX() + ", " + origin.getY());
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    static class Rectangle {

        static final int NUMBER_OF_SIDES = 4;

        private double width;
        private double height;

        Rectangle() {
            this(1, 1);
        }

        Rectangle(double width, double height) {
            setSides(width, height);
        }

        double getWidth() {
            return width;
        }

        void setWidth(double width) {
            setSides(width, height);
        }

        void setSides(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("стороны должны быть положительными");
            }
            this.width = width;
            this.height = height;
        }

        double area() {
            return width * height;
        }

        double perimeter() {
            return 2 * (width + height);
        }

        String description() {
            return "Прямоугольник " + width + " x " + height;
        }
    }

    static class Circle {

        private final Point center;
        private final double radius;

        Circle(Point center, double radius) {
            if (radius <= 0) {
                throw new IllegalArgumentException("радиус должен быть положительным");
            }
            this.center = center;
            this.radius = radius;
        }

        double area() {
            return Math.PI * radius * radius;
        }

        String description() {
            return "Круг с центром (" + center.getX() + ", " + center.getY() + ")";
        }
    }

    static class Student {

        private static int nextId = 1;

        private final int id;
        private final String name;

        Student(String name) {
            id = nextId++;
            this.name = name;
        }

        String description() {
            return "Студент #" + id + ": " + name;
        }
    }

    static final class Point {

        private final double x;
        private final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double getX() {
            return x;
        }

        double getY() {
            return y;
        }
    }

    static final class ImmutableRectangle {

        private final double width;
        private final double height;
        private final Point origin;

        ImmutableRectangle(double width, double height, Point origin) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("стороны должны быть положительными");
            }
            this.width = width;
            this.height = height;
            this.origin = origin;
        }

        Point getOrigin() {
            return origin;
        }

        double area() {
            return width * height;
        }

        String description() {
            return "Неизменяемый прямоугольник " + width + " x " + height;
        }
    }
}
