package creational.objectpool;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Пул ограниченного числа текстовых буферов.
 */
public final class TextBufferPool {

    private final ArrayDeque<TextBuffer> available = new ArrayDeque<>();
    private final Set<TextBuffer> borrowed = Collections.newSetFromMap(new IdentityHashMap<>());
    private final int maxSize;
    private int created;

    public TextBufferPool(int maxSize) {
        this.maxSize = maxSize;
    }

    public TextBuffer borrow() {
        TextBuffer buffer = available.pollFirst();
        if (buffer == null) {
            if (created == maxSize) {
                throw new IllegalStateException("Пул исчерпан");
            }

            buffer = new TextBuffer();
            created++;
        }

        borrowed.add(buffer);
        return buffer;
    }

    public void release(TextBuffer buffer) {
        if (!borrowed.remove(buffer)) {
            throw new IllegalArgumentException("Объект не был взят из этого пула");
        }

        buffer.clear();
        available.addFirst(buffer);
    }
}
