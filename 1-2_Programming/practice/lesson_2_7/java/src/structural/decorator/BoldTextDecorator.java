package structural.decorator;

/**
 * Добавляет жирное оформление.
 */
public final class BoldTextDecorator extends TextDecorator {

    public BoldTextDecorator(Text text) {
        super(text);
    }

    @Override
    public String render() {
        return "<b>" + text.render() + "</b>";
    }
}
