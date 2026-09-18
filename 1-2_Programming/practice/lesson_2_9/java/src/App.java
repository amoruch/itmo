import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * Урок 2.9 — вспомогательные классы.
 */
public class App {

    public static void main(String[] args) throws Exception {
        randomDemo();
        formatsDemo();
        javadocDemo();
    }

    // --- java.util.Random ------------------------------------------------

    static void randomDemo() {
        // Конструкторы: Random() — сид от системы; Random(long seed) — детерминированный.
        Random r = new Random(42);
        System.out.println(r.nextInt());        // любой int
        System.out.println(r.nextInt(100));     // [0, 100)
        System.out.println(r.nextLong());
        System.out.println(r.nextDouble());     // [0.0, 1.0)
        System.out.println(r.nextBoolean());
        System.out.println(r.nextGaussian());   // N(0, 1)

        // Стримы: ints(), longs(), doubles() — бесконечные / с границей.
        r.ints(5, 0, 10).forEach(System.out::println);

        // ThreadLocalRandom — для многопоточного использования.
        // SecureRandom — для криптографических нужд.
    }

    // --- Форматы данных: CSV / JSON / XML --------------------------------

    /** Один объект — три представления в разных форматах. */
    record User(int id, String name) {}

    static void formatsDemo() throws Exception {
        User u = new User(1, "Ivan");

        // Java serialization (для сравнения) — бинарный, AC ED 00 05...
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) { out.writeObject(u); }
        System.out.println("java-ser: " + bytes.size() + " bytes");

        // CSV — простой текст, строки и разделители.
        // Стандартных классов нет → OpenCSV, Apache Commons CSV.
        //   var w = new CSVWriter(new FileWriter("u.csv"));
        //   w.writeNext(new String[]{ ""+u.id(), u.name() });
        // Ручной вариант — println / format:
        var csvOut = new StringWriter();
        try (var w = new PrintWriter(csvOut)) {
            w.println("id,name");
            w.printf("%d,\"%s\"%n", u.id(), u.name());
        }
        System.out.println(csvOut);

        // JSON — сторонние GSON / Jackson, в Java EE — javax.json.
        //   var gson = new Gson();
        //   String json = gson.toJson(u);        // {"id":1,"name":"Ivan"}
        //   User back = gson.fromJson(json, User.class);
        System.out.println("{\"id\":" + u.id() + ",\"name\":\"" + u.name() + "\"}");

        // XML — XStream / JacksonXML / Jakarta XML Bind.
        //   var xs = new XStream();
        //   String xml = xs.toXML(u);            // <User><id>1</id>...
        //   User back = (User) xs.fromXML(xml);
        System.out.println("<User><id>" + u.id() + "</id><name>" + u.name() + "</name></User>");

        // Модели обработки:
        //   Binding — XML/JSON <-> объект целиком (JAXB, GSON);
        //   DOM     — дерево узлов (универсально, много памяти);
        //   SAX/StAX — событийный/потоковый (быстро, мало памяти).
    }

    // --- Javadoc ---------------------------------------------------------

    static void javadocDemo() {
        // Javadoc — комментарии /** ... */, из которых javadoc генерирует HTML.
        // Первое предложение — summary, дальше — основное описание.
        // Теги класса:  @author, @version, @since, @see, @deprecated
        // Теги метода:  @param, @return, @throws
        // Разметка:     {@link X#m()}, {@code List<String>}, {@literal < >}
        // Пакет:        package-info.java
        // Генерация:    javadoc -d doc -author -version *.java
        // Также: mvn javadoc:javadoc, gradle javadoc.
        // Пишем не «как?», а «что?» и «зачем?».
        System.out.println("javadoc -d doc *.java");
    }

    /**
     * Удваивает значение.
     *
     * @param x входное число
     * @return x * 2
     */
    static int doubleX(int x) { return x * 2; }

    /** Сканер как вспомогательное средство разбора строк. */
    static void scannerAux() {
        try (Scanner sc = new Scanner("a;b;c").useDelimiter(";")) {
            while (sc.hasNext()) System.out.println(sc.next());
        }
    }
}
