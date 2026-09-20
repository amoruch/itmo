package creational.factorymethod;

/**
 * Конкретный продукт для морской доставки.
 */
public final class Ship implements Transport {

    @Override
    public void deliver() {
        System.out.println("Груз доставлен кораблём.");
    }
}
