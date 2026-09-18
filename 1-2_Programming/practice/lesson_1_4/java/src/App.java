/**
 * Урок 1.4 — наследование.
 */
public class App {

    public static void main(String[] args) {
        // Наследование: Square IS-A Rectangle. extends + super(...)
        Rectangle rect = new Rectangle(3, 4);
        Square sq = new Square(5);
        System.out.println(rect.descr() + " S=" + rect.area());
        System.out.println(sq.descr() + " S=" + sq.area()); // descr/area унаследованы

        // Переопределение (overriding) — то же имя и параметры.
        Shape shape = new Circle(2);
        System.out.println(shape.area()); // вызывается Circle.area()

        // Статический метод не переопределяется, а скрывается (hiding).
        Base b = new Base();
        Base bExt = new Ext();
        System.out.println(b.statVal());    // 3 — по типу ссылки
        System.out.println(bExt.statVal()); // 3 — статика не полиморфна

        // Полиморфизм: метод экземпляра определяется реальным типом объекта.
        System.out.println(b.getVal());    // 1
        System.out.println(bExt.getVal()); // 2 — Ext переопределил

        // Аннотация @Override ловит опечатки в имени/параметрах.
        // Нельзя сужать доступ при переопределении: protected -> private запрещено.

        // Ссылка на предка может указывать на объект-потомок (upcasting).
        Shape s = new Circle(1.5);
        System.out.println(s instanceof Shape);   // true
        System.out.println(s instanceof Circle);  // true
        System.out.println(s instanceof String);  // false

        // Downcasting — только если реальный тип совпадает, иначе ClassCastException.
        if (s instanceof Circle c) {              // pattern matching (Java 16+)
            System.out.println("radius = " + c.getRadius());
        }

        // Object — корень иерархии. Даже пустой класс уже умеет:
        Object obj = new Object();
        System.out.println(obj.equals(null)); // false
        System.out.println(obj.toString());   // java.lang.Object@...
        System.out.println(obj.getClass());   // class java.lang.Object

        // equals/hashCode/toString переопределяются в своём классе.
        Rectangle r1 = new Rectangle(2, 3);
        Rectangle r2 = new Rectangle(2, 3);
        System.out.println(r1.equals(r2)); // true (значения полей)
        System.out.println(r1);            // Rectangle 2.0x3.0

        // final: запрет изменения/переопределения/наследования.
        // sealed: контролируемое наследование (Java 17+).
        // enum и record — специальные виды классов.

        // Enum: список констант + поля и методы.
        System.out.println(Season.SUMMER.days() + " " + Season.SUMMER);

        // Record: неизменяемый класс-данные. Авто-геттеры, equals, hashCode, toString.
        Point p1 = new Point(2, 3);
        Point p2 = new Point(2, 3);
        System.out.println(p1.equals(p2) + " " + p1); // true Point[x=2, y=3]

        // Обёртки и автоупаковка.
        Integer a = 42;         // Integer.valueOf(42) — из кэша [-128..127]
        Integer b2 = 42;
        Integer c = 451, d = 451; // вне кэша — разные объекты
        System.out.println(a == b2);          // true
        System.out.println(c == d);           // false
        System.out.println(c.equals(d));      // true
    }

    /** Предок: общие поля и методы. */
    static class Shape {
        public double area() { return 0; }
        public String descr() { return "Shape"; }
        public int getVal() { return 1; }
        public static int statVal() { return 3; }
    }

    /** Потомок Square: добавляет/переиспользует. */
    static class Rectangle extends Shape {
        private double width, height;
        Rectangle(double w, double h) { width = w; height = h; }
        @Override public double area()  { return width * height; }
        @Override public String descr() { return "Rectangle " + width + "x" + height; }
        @Override public String toString() { return descr(); }
        @Override public boolean equals(Object o) {
            if (!(o instanceof Rectangle r)) return false;
            return width == r.width && height == r.height;
        }
        @Override public int hashCode() { return Double.hashCode(width * 31 + height); }
    }

    static class Square extends Rectangle {
        Square(double side) { super(side, side); } // вызов конструктора предка
    }

    static class Circle extends Shape {
        private final double radius;
        Circle(double r) { radius = r; }
        @Override public double area() { return Math.PI * radius * radius; }
        double getRadius() { return radius; }
    }

    static class Base { public int getVal() { return 1; } public static int statVal() { return 3; } }
    static class Ext extends Base {
        @Override public int getVal() { return 2; }
        public static int statVal() { return 4; } // hiding, не overriding
    }

    /** Enum с полями и методами. */
    enum Season {
        WINTER(90), SPRING(92), SUMMER(92), AUTUMN(91);
        private final int days;
        Season(int d) { days = d; }
        int days() { return days; }
    }

    /** Record — неизменяемые данные. */
    record Point(int x, int y) {}
}
