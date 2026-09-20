package structural.proxy;

/**
 * Реальный объект, загрузка которого стоит дорого.
 */
public final class RealImage implements Image {

    private final String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        System.out.println("Загрузка файла " + fileName);
    }

    @Override
    public void show() {
        System.out.println("Показ изображения " + fileName);
    }
}
