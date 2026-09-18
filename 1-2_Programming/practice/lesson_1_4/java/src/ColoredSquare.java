
class ColoredSquare extends Square {

    private final String color;

    ColoredSquare(double side, String color) {
        super(side);
        this.color = color;
    }

    String color() {
        return color;
    }

    @Override
    String description() {
        return "%s (%s)".formatted(super.description(), color);
    }
}
