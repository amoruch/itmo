package structural.proxy;

/**
 * Виртуальный прокси, откладывающий создание RealImage.
 */
public final class ImageProxy implements Image {

    private final String fileName;
    private RealImage realImage;

    public ImageProxy(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void show() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }

        realImage.show();
    }
}
