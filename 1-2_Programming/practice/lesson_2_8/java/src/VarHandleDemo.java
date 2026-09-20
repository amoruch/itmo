
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * Атомарные операции с полем через VarHandle.
 */
public final class VarHandleDemo {

    private VarHandleDemo() {
    }

    public static void run() throws ReflectiveOperationException {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        VarHandle value = lookup.findVarHandle(Counter.class, "value", int.class);
        Counter counter = new Counter();

        boolean updated = value.compareAndSet(counter, 0, 10);
        int previous = (int) value.getAndAdd(counter, 5);

        System.out.println("compareAndSet = " + updated);
        System.out.println("previous = " + previous + ", current = " + counter.getValue());
    }
}
