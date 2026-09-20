package structural.decorator;

/**
 * Базовый компонент без дополнительного оформления.
 */
public final class PlainText implements Text {

    private final String value;

    public PlainText(String value) {
        this.value = value;
    }

    @Override
    public String render() {
        return value;
    }
}
