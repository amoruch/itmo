package structural.decorator;

/**
 * Демонстрация последовательной композиции декораторов.
 */
public final class DecoratorDemo {

    private DecoratorDemo() {
    }

    public static void run() {
        Text text = new PlainText("Java");
        Text quotedText = new QuotedTextDecorator(text);
        Text boldQuotedText = new BoldTextDecorator(quotedText);

        System.out.println(boldQuotedText.render());
    }
}
