/**
 * Урок 1.5 — полиморфизм и интерфейсы.
 */
public class App {

    public static void main(String[] args) {
        // Полиморфизм подтипов: одна ссылка Shape — разные реальные объекты.
        Shape[] shapes = { new Rectangle(2, 3), new Square(4), new Circle(1.5) };
        for (Shape s : shapes) {
            System.out.printf("%s area=%.2f per=%.2f%n", s, s.area(), s.perimeter());
        }

        // Динамическое связывание: метод выбирается по реальному типу объекта.

        // Абстрактный класс — нельзя new, но можно ссылку. Общий код + abstract методы.
        // Shape s = new Shape(); // ошибка

        // Интерфейс — контракт без состояния. Класс реализует сколько угодно интерфейсов.
        Flyable[] flying = { new Bird(), new Airplane(), new FlyingFish() };
        for (Flyable f : flying) f.fly();

        // Методы интерфейса: abstract (по умолчанию), default, static, private.
        Duck duck = new Duck();
        duck.swim();   // abstract
        duck.walk();   // default из Walkable
        duck.fly();    // default из Flyable

        // Comparator — внешний порядок; Comparable — естественный.
        String[] dow = { "sunday", "monday", "tuesday", "friday" };
        java.util.Arrays.sort(dow, new java.util.Comparator<String>() {
            @Override public int compare(String a, String b) {
                return a.length() - b.length(); // анонимный класс
            }
        });
        System.out.println(java.util.Arrays.toString(dow));

        // Вложенные классы: static, inner, local, anonymous.
        Outer.StaticNested sn = new Outer.StaticNested(); // без экземпляра Outer
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();            // связан с экземпляром
        System.out.println(sn + " " + inner);

        // ArrayList — динамический массив, только ссылочные типы.
        var list = new java.util.ArrayList<Integer>();
        list.add(1); list.add(2); list.add(0, 0);
        System.out.println(list + " size=" + list.size());

        // List.of — неизменяемый; чтобы менять — обернуть в ArrayList.
        var fixed = java.util.List.of(1, 2, 3);
        var mutable = new java.util.ArrayList<>(fixed);
        mutable.add(4);

        // Сканер: разбор ввода по строкам и числам.
        var sc = new java.util.Scanner("10 20\n30 40");
        while (sc.hasNextInt()) System.out.print(sc.nextInt() + " ");
        System.out.println();
        sc.close();

        // java.time (Java 8+): LocalDate / LocalTime / LocalDateTime / ZonedDateTime / Period / Duration.
        var d1 = java.time.LocalDate.of(2026, 3, 10);
        var d2 = java.time.LocalDate.parse("2026-01-01");
        var firstOfMonth = java.time.LocalDate.now()
                .with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());

        var dt = java.time.LocalDateTime.now().minusHours(2).plusMinutes(30);
        var moscow = java.time.ZonedDateTime.of(dt, java.time.ZoneId.of("Europe/Moscow"));

        var period = java.time.Period.between(d2, d1);                  // P2M9D
        var duration = java.time.Duration.between(dt, java.time.LocalDateTime.now());
        System.out.println(period + " / " + duration + " / " + moscow.getZone());

        // Regex: Pattern + Matcher; группы захвата через скобки.
        var m = java.util.regex.Pattern.compile("(\\w+)@(\\w+\\.)+\\w{2,}")
                .matcher("user@se.itmo.ru");
        if (m.matches()) System.out.println("user=" + m.group(1));
    }

    // --- Абстрактный класс Shape -----------------------------------------

    abstract static class Shape {
        public abstract double area();
        public abstract double perimeter();
        @Override public String toString() { return getClass().getSimpleName(); }
    }

    static class Rectangle extends Shape {
        protected double width, height;
        Rectangle(double w, double h) { width = w; height = h; }
        @Override public double area() { return width * height; }
        @Override public double perimeter() { return 2 * (width + height); }
    }

    static class Square extends Rectangle {
        Square(double side) { super(side, side); }
        @Override public String toString() { return "Square"; }
    }

    static class Circle extends Shape {
        private final double r;
        Circle(double r) { this.r = r; }
        @Override public double area() { return Math.PI * r * r; }
        @Override public double perimeter() { return 2 * Math.PI * r; }
    }

    // --- Интерфейсы ------------------------------------------------------

    interface Flyable {
        void fly(); // public abstract по умолчанию
        default void land() { System.out.println("landing..."); }
        static Flyable of() { return new Bird(); } // static — принадлежит интерфейсу
    }

    interface Swimmable { void swim(); }
    interface Walkable  { default void walk() { System.out.println("walk"); } }

    /** Класс реализует несколько интерфейсов — разные аспекты поведения. */
    static class Duck implements Flyable, Swimmable, Walkable {
        @Override public void fly()  { System.out.println("duck fly"); }
        @Override public void swim() { System.out.println("duck swim"); }
    }

    static class Bird   implements Flyable { @Override public void fly() { System.out.println("bird fly"); } }
    static class Airplane implements Flyable { @Override public void fly() { System.out.println("plane fly"); } }
    static class FlyingFish implements Flyable { @Override public void fly() { System.out.println("fish fly"); } }

    // --- Вложенные классы ------------------------------------------------

    static class Outer {
        static class StaticNested {}                 // static — без экземпляра Outer
        class Inner {}                               // inner — связан с Outer
        void m() {
            class Local {}                           // local — виден только в методе
            new Local();
        }
    }
}
