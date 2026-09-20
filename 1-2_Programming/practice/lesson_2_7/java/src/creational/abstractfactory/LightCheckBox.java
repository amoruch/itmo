package creational.abstractfactory;

/**
 * Светлый флажок.
 */
public final class LightCheckBox implements CheckBox {

    @Override
    public void draw() {
        System.out.println("Светлый флажок");
    }
}
