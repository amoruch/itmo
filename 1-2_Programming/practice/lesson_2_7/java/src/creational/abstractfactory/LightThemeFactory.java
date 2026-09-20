package creational.abstractfactory;

/**
 * Фабрика светлого семейства компонентов.
 */
public final class LightThemeFactory implements ThemeFactory {

    @Override
    public Button createButton() {
        return new LightButton();
    }

    @Override
    public CheckBox createCheckBox() {
        return new LightCheckBox();
    }
}
