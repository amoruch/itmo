package structural.decorator;

/**
 * Добавляет кавычки вокруг текста.
 */
public final class QuotedTextDecorator extends TextDecorator {

    public QuotedTextDecorator(Text text) {
        super(text);
    }

    @Override
    public String render() {
        return "\"" + text.render() + "\"";
    }
}
