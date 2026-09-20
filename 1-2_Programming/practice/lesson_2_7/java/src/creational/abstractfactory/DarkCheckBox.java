package creational.abstractfactory;

/**
 * Тёмный флажок.
 */
public final class DarkCheckBox implements CheckBox {

    @Override
    public void draw() {
        System.out.println("Тёмный флажок");
    }
}
