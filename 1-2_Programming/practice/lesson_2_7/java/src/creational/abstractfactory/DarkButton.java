package creational.abstractfactory;

/**
 * Тёмная кнопка.
 */
public final class DarkButton implements Button {

    @Override
    public void draw() {
        System.out.println("Тёмная кнопка");
    }
}
