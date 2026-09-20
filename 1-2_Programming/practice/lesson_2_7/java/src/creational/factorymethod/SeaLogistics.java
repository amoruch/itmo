package creational.factorymethod;

/**
 * Создатель корабля.
 */
public final class SeaLogistics extends Logistics {

    @Override
    protected Transport createTransport() {
        return new Ship();
    }
}
