package creational.singleton;

/**
 * Единственный объект настроек приложения.
 */
public final class ApplicationSettings {

    private static final ApplicationSettings INSTANCE = new ApplicationSettings();

    private int requests;

    private ApplicationSettings() {
    }

    public static ApplicationSettings getInstance() {
        return INSTANCE;
    }

    public void registerRequest() {
        requests++;
    }

    public int getRequests() {
        return requests;
    }
}
