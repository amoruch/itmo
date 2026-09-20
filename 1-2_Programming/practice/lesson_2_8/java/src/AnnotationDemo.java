
import java.lang.reflect.Field;

/**
 * Чтение пользовательских аннотаций во время выполнения.
 */
public final class AnnotationDemo {

    private AnnotationDemo() {
    }

    public static void run() {
        DBTable table = User.class.getAnnotation(DBTable.class);
        System.out.println("table = " + table.name());

        for (Field field : User.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                System.out.println("primary key = " + field.getName());
            }
        }
    }
}
