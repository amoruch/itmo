package structural.decorator;

/**
 * Базовый декоратор, делегирующий работу обёрнутому тексту.
 */
public abstract class TextDecorator implements Text {

    protected final Text text;

    protected TextDecorator(Text text) {
        this.text = text;
    }
}
