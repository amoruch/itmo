
class Square extends Rectangle {

    Square(double side) {
        super(side, side);
    }

    @Override
    String description() {
        return "Квадрат";
    }
}
