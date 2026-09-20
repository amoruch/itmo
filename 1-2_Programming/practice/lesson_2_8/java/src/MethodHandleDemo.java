
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Поиск и вызов метода через MethodHandle.
 */
public final class MethodHandleDemo {

    private MethodHandleDemo() {
    }

    public static void run() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        MethodType type = MethodType.methodType(void.class, String.class);
        MethodHandle hello = lookup.findVirtual(MHHello.class, "hello", type);

        MHHello target = new MHHello();
        hello.invokeExact(target, "world");
    }
}
