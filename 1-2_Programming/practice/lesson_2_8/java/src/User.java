
/** Класс с метаданными, которые читает AnnotationDemo. */
@DBTable(name = "users")
public class User {

    @PrimaryKey
    private int id;
    private String name;
}
