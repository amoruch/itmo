package creational.objectpool;

/**
 * Демонстрация повторного использования объекта из пула.
 */
public final class ObjectPoolDemo {

    private ObjectPoolDemo() {
    }

    public static void run() {
        TextBufferPool pool = new TextBufferPool(2);
        TextBuffer first = pool.borrow();
        first.append("готовый текст");
        System.out.println(first);
        pool.release(first);

        TextBuffer second = pool.borrow();
        System.out.println("reused instance = " + (first == second));
        System.out.println("after reset = '" + second + "'");
        pool.release(second);
    }
}
