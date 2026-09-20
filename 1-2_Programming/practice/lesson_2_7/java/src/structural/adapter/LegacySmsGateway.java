package structural.adapter;

import java.nio.charset.StandardCharsets;

/**
 * Внешний шлюз со старым интерфейсом.
 */
public final class LegacySmsGateway {

    public void transmit(String phone, byte[] message) {
        String text = new String(message, StandardCharsets.UTF_8);
        System.out.println("SMS для " + phone + ": " + text);
    }
}
