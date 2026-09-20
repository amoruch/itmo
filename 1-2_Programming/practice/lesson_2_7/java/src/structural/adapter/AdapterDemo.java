package structural.adapter;

/**
 * Демонстрация object adapter.
 */
public final class AdapterDemo {

    private AdapterDemo() {
    }

    public static void run() {
        LegacySmsGateway gateway = new LegacySmsGateway();
        NotificationSender sender = new SmsGatewayAdapter(gateway);

        sender.send(new Notification("+79990000000", "Практика началась"));
    }
}
