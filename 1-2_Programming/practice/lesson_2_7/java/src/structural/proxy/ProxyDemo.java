package structural.proxy;

/**
 * Демонстрация виртуального прокси.
 */
public final class ProxyDemo {

    private ProxyDemo() {
    }

    public static void run() {
        Image image = new ImageProxy("photo.png");

        System.out.println("Первый показ:");
        image.show();
        System.out.println("Повторный показ:");
        image.show();
    }
}
