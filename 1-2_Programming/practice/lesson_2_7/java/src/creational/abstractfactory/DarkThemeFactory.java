package creational.abstractfactory;

/**
 * Фабрика тёмного семейства компонентов.
 */
public final class DarkThemeFactory implements ThemeFactory {

    @Override
    public Button createButton() {
        return new DarkButton();
    }

    @Override
    public CheckBox createCheckBox() {
        return new DarkCheckBox();
    }
}
