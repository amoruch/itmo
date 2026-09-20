package creational.abstractfactory;

/**
 * Фабрика согласованной темы интерфейса.
 */
public interface ThemeFactory {

    Button createButton();

    CheckBox createCheckBox();
}
