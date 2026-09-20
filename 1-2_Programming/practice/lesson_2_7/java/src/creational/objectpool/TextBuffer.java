package creational.objectpool;

/**
 * Объект, который можно вернуть в пул после использования.
 */
public final class TextBuffer {

    private final StringBuilder content = new StringBuilder();

    public void append(String text) {
        content.append(text);
    }

    public void clear() {
        content.setLength(0);
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
