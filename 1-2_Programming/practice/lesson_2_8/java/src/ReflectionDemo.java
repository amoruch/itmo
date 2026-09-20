
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Получение метаданных и вызов членов через reflection.
 */
public final class ReflectionDemo {

    private ReflectionDemo() {
    }

    public static void run() throws ReflectiveOperationException {
        Class<Person> type = Person.class;
        Person person = type.getDeclaredConstructor(String.class, int.class)
                .newInstance("Alex", 20);

        System.out.println("class = " + type.getName());
        System.out.println("superclass = " + type.getSuperclass().getSimpleName());

        Field name = type.getDeclaredField("name");
        name.setAccessible(true);
        name.set(person, "Sam");

        Method describe = type.getDeclaredMethod("describe");
        describe.setAccessible(true);
        System.out.println("method result = " + describe.invoke(person));

        Class<?> loaded = Class.forName("Person");
        System.out.println("loaded by name = " + loaded.getSimpleName());
    }
}
