package creational.factorymethod;

/**
 * Демонстрация выбора продукта через подкласс создателя.
 */
public final class FactoryMethodDemo {

    private FactoryMethodDemo() {
    }

    public static void run() {
        Logistics roadLogistics = new RoadLogistics();
        Logistics seaLogistics = new SeaLogistics();

        roadLogistics.planDelivery();
        seaLogistics.planDelivery();
    }
}
