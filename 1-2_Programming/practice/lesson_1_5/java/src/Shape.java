abstract class Shape {

    abstract double area();

    abstract double perimeter();

    String description() {
        return "%s: площадь = %.2f, периметр = %.2f"
                .formatted(this, area(), perimeter());
    }
}

class Rectangle extends Shape {

    private final double width;
    private final double height;

    Rectangle(double width, double height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Стороны должны быть положительными");
        }

        this.width = width;
        this.height = height;
    }

    @Override
    double area() {
        return width * height;
    }

    @Override
    double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public String toString() {
        return "Прямоугольник %.1f x %.1f".formatted(width, height);
    }
}

class Circle extends Shape {

    private final double radius;

    Circle(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Радиус должен быть положительным");
        }

        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }

    @Override
    double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String toString() {
        return "Круг радиуса %.1f".formatted(radius);
    }
}