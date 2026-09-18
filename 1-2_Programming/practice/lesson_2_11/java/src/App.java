import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

/**
 * Урок 2.11 — NIO: Buffer.
 */
public class App {

    public static void main(String[] args) {
        bufferAnatomy();
        bufferLifecycle();
        bufferKinds();
        charsetDemo();
    }

    /** Буфер: capacity / limit / position / mark. */
    static void bufferAnatomy() {
        // java.nio.Buffer — контейнер для данных:
        //   capacity — максимальный размер (фиксирован при создании);
        //   limit    — «сколько можно записать/прочитать» (<= capacity);
        //   position — текущая позиция;
        //   mark     — метка для reset().
        //
        // Инвариант: 0 <= mark <= position <= limit <= capacity.
        ByteBuffer buf = ByteBuffer.allocate(10);
        System.out.println("cap=" + buf.capacity() + " lim=" + buf.limit() + " pos=" + buf.position());
        // 10 / 10 / 0
    }

    /** Жизненный цикл: запись → flip → чтение → clear/compact/rewind. */
    static void bufferLifecycle() {
        ByteBuffer buf = ByteBuffer.allocate(10);

        // Запись: clear() → put(...) несколько раз.
        buf.clear();                    // limit = capacity, position = 0
        buf.put((byte) 1).put((byte) 2).put((byte) 3);
        System.out.println("after put: pos=" + buf.position() + " lim=" + buf.limit());
        // pos=3, lim=10

        // Переход к чтению: flip() — limit = position, position = 0.
        buf.flip();
        System.out.println("after flip: pos=" + buf.position() + " lim=" + buf.limit());
        // pos=0, lim=3

        // Чтение: hasRemaining() / get().
        while (buf.hasRemaining()) System.out.print(buf.get() + " ");
        System.out.println();

        // clear()      — подготовить к новой записи (limit=cap, pos=0);
        // compact()    — непрочитанное сдвинуть в начало, потом дописывать;
        // rewind()     — position = 0, читать/писать с начала;
        // mark()/reset() — поставить/вернуть метку.

        // Абсолютный доступ — по индексу, позиция не меняется:
        byte b = buf.get(0);
        // Относительный доступ — по текущей позиции, позиция двигается:
        // buf.get();
    }

    /** Типизированные буферы: Byte / Char / Int / Short / Long / Float / Double. */
    static void bufferKinds() {
        // ByteBuffer, CharBuffer, IntBuffer, ShortBuffer, LongBuffer,
        // FloatBuffer, DoubleBuffer.
        // У каждого — свои getXxx()/putXxx().
        var ib = java.nio.IntBuffer.allocate(3);
        ib.put(1).put(2).put(3);
        ib.flip();
        while (ib.hasRemaining()) System.out.print(ib.get() + " ");
        System.out.println();

        // Порядок байтов: BIG_ENDIAN / LITTLE_ENDIAN / nativeOrder().
        var bb = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(0x01020304);
        System.out.println("order=" + bb.order());
    }

    /** Charset: декодирование байтов в символы и обратно. */
    static void charsetDemo() {
        var cs = java.nio.charset.StandardCharsets.UTF_8;
        var encoder = cs.newEncoder();
        var decoder = cs.newDecoder();

        // Полный цикл: encode(CharBuffer) → ByteBuffer; decode(ByteBuffer) → CharBuffer.
        CharBuffer cb = CharBuffer.wrap("Привет");
        ByteBuffer bb = cs.encode(cb);
        CharBuffer back = cs.decode(bb);
        System.out.println("decoded=" + back.toString());
    }
}
