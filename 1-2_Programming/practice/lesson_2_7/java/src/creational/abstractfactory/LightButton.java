package creational.abstractfactory;

/**
 * Светлая кнопка.
 */
public final class LightButton implements Button {

    @Override
    public void draw() {
        System.out.println("Светлая кнопка");
    }
}
