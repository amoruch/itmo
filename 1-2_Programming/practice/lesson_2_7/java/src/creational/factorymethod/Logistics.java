package creational.factorymethod;

/**
 * Базовый создатель с общим алгоритмом доставки.
 */
public abstract class Logistics {

    public final void planDelivery() {
        Transport transport = createTransport();
        transport.deliver();
    }

    protected abstract Transport createTransport();
}
