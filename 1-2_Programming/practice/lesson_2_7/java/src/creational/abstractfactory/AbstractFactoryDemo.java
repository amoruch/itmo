package creational.abstractfactory;

/**
 * Демонстрация создания согласованных семейств объектов.
 */
public final class AbstractFactoryDemo {

    private AbstractFactoryDemo() {
    }

    public static void run() {
        render(new LightThemeFactory());
        render(new DarkThemeFactory());
    }

    private static void render(ThemeFactory factory) {
        Button button = factory.createButton();
        CheckBox checkBox = factory.createCheckBox();

        button.draw();
        checkBox.draw();
    }
}
