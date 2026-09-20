package structural.adapter;

/**
 * Уведомление в формате приложения.
 */
public final class Notification {

    private final String recipient;
    private final String text;

    public Notification(String recipient, String text) {
        this.recipient = recipient;
        this.text = text;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getText() {
        return text;
    }
}
