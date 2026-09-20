package creational.factorymethod;

/**
 * Конкретный продукт для доставки по дороге.
 */
public final class Truck implements Transport {

    @Override
    public void deliver() {
        System.out.println("Груз доставлен грузовиком.");
    }
}
