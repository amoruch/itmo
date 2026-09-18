import java.util.Map;
import java.util.ServiceLoader;

/**
 * Урок 2.25 — провайдеры служб (ServiceLoader).
 */
public class App {

    public static void main(String[] args) {
        problemStatement();
        serviceLocatorPattern();
        serviceLoaderDemo();
        builtinProviders();
    }

    /** Зачем нужны провайдеры: разные реализации одной службы. */
    static void problemStatement() {
        // Служба (сервис) — интерфейс, разные реализации под разные случаи.
        // Нужен механизм: как найти и загрузить подходящую реализацию,
        // не прописывая её жёстко в коде.

        // Пример: CheatSheet, у которого есть источник знаний (Knowledge).
        // Реализации Knowledge: Memory, Lecture, CallFriend, Miracle.
        // Хочется менять реализацию без правок CheatSheet.
    }

    /** Шаблон ServiceLocator. */
    static void serviceLocatorPattern() {
        // ServiceLocator — «локатор»: клиент спрашивает сервис по имени.
        //   Service getService()
        //   InitialContext.lookup(...) — достаёт реализацию из каталога.
        //
        // Схема:
        //   Client → ServiceLocator → Service ← Service1, Service2
        //                                    ↑
        //                                InitialContext
        //
        // Минус: клиент знает про локатор; зависимости не видны в сигнатурах.
    }

    /** java.util.ServiceLoader — стандартный механизм поиска. */
    static void serviceLoaderDemo() {
        // ServiceLoader.load(Service.class) — находит все реализации
        // указанного интерфейса в classpath / module-path.
        //
        // Порядок поиска:
        //   classpath: каталог META-INF/services/<FQN-интерфейса>,
        //              файл содержит список FQN классов-реализаций.
        //   module-path:  provides <Service> with <Impl1>, <Impl2>;
        //
        // Метод:  static <S> ServiceLoader<S> load(Class<S> service)
        //         Iterator<S> iterator()
        //         stream()
        //         reload()

        ServiceLoader<Knowledge> loader = ServiceLoader.load(Knowledge.class);
        for (Knowledge k : loader) {
            System.out.println(k.getSource());
        }

        // Пример структуры для classpath:
        // service.jar
        //   META-INF/services/spi.Knowledge       ← имя интерфейса
        //     spi.Memory                          ← FQN реализации
        //     spi.Lecture

        // В модуле (module-info.java):
        //   uses spi.Knowledge;
        //   provides spi.Knowledge with spi.Memory, spi.Lecture;
    }

    /** Где в Java API используются провайдеры. */
    static void builtinProviders() {
        // Классические примеры ServiceLoader внутри JDK:
        //   java.sql.DriverManager                        — драйверы JDBC;
        //   java.nio.file.spi.FileSystemProvider          — разные FS;
        //   java.nio.channels.spi.AsynchronousChannelProvider;
        //   java.nio.channels.spi.SelectorProvider;
        //   java.nio.charset.spi.CharsetProvider          — кодировки;
        //   java.text.spi.DateTimeFormatProvider;
        //   java.text.spi.NumberFormatProvider;
        //   java.util.spi.CalendarDataProvider.
    }

    /** Интерфейс службы для демонстрации. */
    interface Knowledge {
        Map<String, String> getSource();
    }
}
