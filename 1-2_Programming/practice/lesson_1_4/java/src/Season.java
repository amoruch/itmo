
enum Season {
    WINTER("холодно"),
    SPRING("теплеет"),
    SUMMER("тепло"),
    AUTUMN("прохладно");

    private final String description;

    Season(String description) {
        this.description = description;
    }

    String description() {
        return description;
    }
}
