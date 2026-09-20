package creational.factorymethod;

/**
 * Создатель грузовика.
 */
public final class RoadLogistics extends Logistics {

    @Override
    protected Transport createTransport() {
        return new Truck();
    }
}
