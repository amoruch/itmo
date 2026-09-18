
class Vegetable {

    private final String name;

    Vegetable(String name) {
        this.name = name;
    }

    void harvest(double strength) throws VeggyBreakException {
        if (strength <= 0) {
            throw new VeggyBreakException(
                    "%s разломилась".formatted(name),
                    new IllegalStateException("strength = " + strength)
            );
        }
    }
}
