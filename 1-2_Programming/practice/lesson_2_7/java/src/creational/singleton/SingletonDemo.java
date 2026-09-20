package creational.singleton;

/**
 * Демонстрация eager Singleton.
 */
public final class SingletonDemo {

    private SingletonDemo() {
    }

    public static void run() {
        ApplicationSettings first = ApplicationSettings.getInstance();
        ApplicationSettings second = ApplicationSettings.getInstance();
        first.registerRequest();

        System.out.println("same instance = " + (first == second));
        System.out.println("requests = " + second.getRequests());
    }
}
