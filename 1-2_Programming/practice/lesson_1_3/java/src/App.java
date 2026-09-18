/**
 * Урок 1.3 — основы ООП.
 */
public class App {

    public static void main(String[] args) {
        // Класс — шаблон, объект — экземпляр. Поля = состояние, методы = поведение.
        Rectangle r1 = new Rectangle(3.0, 4.0);
        Rectangle r2 = new Rectangle(5.0, 6.0);
        System.out.println(r1.descr() + " S=" + r1.area());
        System.out.println(r2.descr() + " S=" + r2.area());

        // Ссылки: r3 и r1 указывают на один объект.
        Rectangle r3 = r1;
        r3.setWidth(10.0);
        System.out.println("r1.width = " + r1.getWidth()); // 10.0

        // null — неинициализированная ссылка.
        Rectangle r4 = null;
        // r4.area(); // NullPointerException

        // Композиция: объект как поле.
        Circle c = new Circle(new Point(1, 2), 5.0);
        System.out.println("Circle center: " + c.getCenter().x + "," + c.getCenter().y);

        // Статические поля и методы — принадлежат классу, не объекту.
        System.out.println("sides = " + Rectangle.NUM_OF_SIDES);
        System.out.println("maxID = " + Counter.nextId());

        // Изменяемость: final-поле + изменяемый объект внутри — не иммутабельно.
        Point mutable = new Point(0, 0);
        Immutable imm = new Immutable(1.0, mutable);
        mutable.x = 99; // "испортили" immutable извне
        System.out.println("imm point.x = " + imm.getPoint().x);
    }

    /** Класс: поля + методы. */
    static class Rectangle {
        private double width, height;
        static final int NUM_OF_SIDES = 4;

        // Конструктор: имя класса, без возвращаемого типа.
        Rectangle(double w, double h) { setSides(w, h); }

        // Перегрузка конструкторов через this(...).
        Rectangle() { this(1.0, 1.0); }

        public double getWidth()  { return width; }
        public double getHeight() { return height; }

        public void setWidth(double w)  { if (w > 0) width = w; }
        public void setHeight(double h) { if (h > 0) height = h; }
        public void setSides(double w, double h) { setWidth(w); setHeight(h); }

        public double area() { return width * height; }
        public String descr() { return "Rectangle " + width + "x" + height; }
    }

    /** Композиция: Rectangle-like хранит Point. */
    static class Circle {
        private final Point center;
        private final double radius;
        Circle(Point c, double r) { center = c; radius = r; }
        Point getCenter() { return center; }
        double area() { return Math.PI * radius * radius; }
    }

    /** Простой класс-точка. */
    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
    }

    /** Статическое поле — общее для класса. */
    static class Counter {
        private static int id = 0;
        static int nextId() { return ++id; }
    }

    /** Иммутабельность: все поля final И все вложенные объекты неизменяемы. */
    static class Immutable {
        private final double width;
        private final Point point; // Point изменяемый — иммутабельность нарушена
        Immutable(double w, Point p) { width = w; point = p; }
        Point getPoint() { return point; }
    }
}
