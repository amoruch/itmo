
/** Урок 2.8 — рефлексия, аннотации и инструменты. */
public class App {

    public static void main(String[] args) throws Throwable {
        part1Reflection();
        part2MethodHandles();
        part3VarHandles();
        part4Annotations();
        part5Tools();
    }

    static void part1Reflection() throws ReflectiveOperationException {
        System.out.println("part1 - reflection");
        ReflectionDemo.run();
    }

    static void part2MethodHandles() throws Throwable {
        System.out.println("part2 - MethodHandle");
        MethodHandleDemo.run();
    }

    static void part3VarHandles() throws ReflectiveOperationException {
        System.out.println("part3 - VarHandle");
        VarHandleDemo.run();
    }

    static void part4Annotations() {
        System.out.println("part4 - annotations");
        AnnotationDemo.run();
    }

    static void part5Tools() {
        System.out.println("part5 - tools");
        ToolingDemo.run();
    }
}
