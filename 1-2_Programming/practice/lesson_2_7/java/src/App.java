import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Урок 2.7 — сериализация объектов.
 */
public class App {

    public static void main(String[] args) throws Exception {
        basics();
        hierarchy();
        transientAndStatic();
        versioning();
        customWriteRead();
        externalizable();
        risks();
    }

    /** Основы: пишем объект в поток байтов и читаем обратно. */
    static void basics() throws Exception {
        var user = new User(1, "Ivan");

        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) {
            out.writeObject(user);
        }
        System.out.println("size=" + bytes.size()); // несколько десятков байт

        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            User back = (User) in.readObject();
            System.out.println(back);
        }

        // Признак «можно сериализовать» — интерфейс-маркер Serializable (без методов).
        // Порядок: класс + сигнатура класса + значения нестатических и
        // нетранзиентных полей самого класса и всех его суперклассов.
    }

    /** Иерархия: пока Serializable не у всех — не сериализуется (NotSerializableException). */
    static void hierarchy() throws Exception {
        // Если у суперкласса нет Serializable, но у наследника есть,
        // конструктор суперкласса не вызывается (объект создаётся в обход new).
        // Все нестатические поля этого суперкласса должны быть доступны через
        // его публичный/protected конструктор без аргументов.
        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(new Sub());
    }

    /** Статические и transient-поля не сериализуются. */
    static void transientAndStatic() throws Exception {
        var o = new WithTransient(1, "password", 42);
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) { out.writeObject(o); }
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            System.out.println(in.readObject()); // password=null, static сброшен
        }
    }

    /** Версионирование: serialVersionUID фиксирует сигнатуру класса. */
    static void versioning() throws Exception {
        // Если UID в классе и в потоке не совпадают — InvalidClassException.
        // Явно задаём: private static final long serialVersionUID = 1L;
        // Если не задан — вычисляется компилятором из структуры класса.
        // Опасность: любое изменение полей/методов ломает совместимость.
    }

    /** Своя логика: writeObject / readObject внутри класса. */
    static void customWriteRead() throws Exception {
        var c = new Cipher("secret");
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) { out.writeObject(c); }
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            System.out.println(in.readObject()); // уже расшифровано
        }
    }

    /** Externalizable — полный контроль, но суперкласс сериализуем сами. */
    static void externalizable() throws Exception {
        var e = new Ext(7, "x");
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) { out.writeObject(e); }
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            System.out.println(in.readObject());
        }
    }

    /** Минусы: несовместимость версий, размер, глубина стека, обход конструкторов. */
    static void risks() {
        // Сериализованный формат зависит от структуры класса.
        // Объекты могут создаваться в обход конструкторов → инварианты не проверяются.
        // Опасные (crafted) потоки могут привести к неожиданной загрузке классов.
        // Большая вложенность → StackOverflow при глубокой структуре.
        // Для долгого хранения/обмена обычно лучше JSON/XML.
    }

    // --- Модели ----------------------------------------------------------

    static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        int id;
        String name;
        User(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return "User(" + id + "," + name + ")"; }
    }

    static class Base { int base = 1; }
    static class Sub extends Base implements Serializable { int sub = 2; }

    static class WithTransient implements Serializable {
        int id;
        transient String password; // не сериализуется
        static int counter = 42;   // статическое — общее для класса, не пишется в поток
        WithTransient(int id, String p, int c) { this.id = id; this.password = p; counter = c; }
        @Override public String toString() { return id + "/" + password + "/" + counter; }
    }

    /** Своя сериализация: пишем зашифрованные байты, читаем и расшифровываем. */
    static class Cipher implements Serializable {
        private static final long serialVersionUID = 1L;
        private transient String value;

        Cipher(String v) { value = v; }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            byte[] enc = new byte[value.length()];
            for (int i = 0; i < value.length(); i++) enc[i] = (byte) (value.charAt(i) ^ 0xFF);
            out.writeInt(enc.length);
            out.write(enc);
        }
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            int len = in.readInt();
            byte[] enc = in.readNBytes(len);
            char[] dec = new char[len];
            for (int i = 0; i < len; i++) dec[i] = (char) (enc[i] ^ 0xFF);
            value = new String(dec);
        }
        @Override public String toString() { return "Cipher(" + value + ")"; }
    }

    /** Externalizable — полностью свой формат, суперкласс обрабатываем сами. */
    static class Ext implements Externalizable {
        int id;
        String name;
        public Ext() {}                    // обязательный public no-arg конструктор
        Ext(int id, String name) { this.id = id; this.name = name; }
        @Override public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(id);
            out.writeUTF(name);
        }
        @Override public void readExternal(ObjectInput in) throws IOException {
            id = in.readInt();
            name = in.readUTF();
        }
        @Override public String toString() { return "Ext(" + id + "," + name + ")"; }
    }
}
