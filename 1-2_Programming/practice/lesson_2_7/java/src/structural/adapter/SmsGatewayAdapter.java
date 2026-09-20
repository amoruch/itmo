package structural.adapter;

import java.nio.charset.StandardCharsets;

/**
 * Адаптер интерфейса уведомлений к старому SMS-шлюзу.
 */
public final class SmsGatewayAdapter implements NotificationSender {

    private final LegacySmsGateway gateway;

    public SmsGatewayAdapter(LegacySmsGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void send(Notification notification) {
        byte[] message = notification.getText().getBytes(StandardCharsets.UTF_8);
        gateway.transmit(notification.getRecipient(), message);
    }
}
