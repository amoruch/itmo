import java.lang.reflect.InvocationHandler;
import java.net.URI;
import java.net.URL;

/**
 * Урок 2.17 — URL / URLConnection и шаблон Proxy.
 */
public class App {

    public static void main(String[] args) throws Exception {
        uriVsUrl();
        urlDemo();
        urlConnectionDemo();
        proxyPatternDemo();
        dynamicProxyDemo();
    }

    /** URI / URL / URN — иерархия понятий. */
    static void uriVsUrl() throws Exception {
        // URI — Unified Resource Identifier: идентификатор ресурса.
        //   URL — Unified Resource Locator:  «где найти» (адрес).
        //   URN — Unified Resource Name:     «имя» (например, urn:isbn:...).
        //
        // Пример: http://example.com:80/path?q=1#frag
        //   scheme= http, host= example.com, port= 80,
        //   path= /path, query= q=1, fragment= frag.

        URI uri = new URI("https://example.com:443/api?x=1#top");
        System.out.println(uri.getScheme() + "://" + uri.getHost() + ":" + uri.getPort());
        System.out.println("path=" + uri.getPath() + " query=" + uri.getQuery());

        // URL — подкласс URI, умеет открывать соединение.
        URL url = uri.toURL();
        System.out.println("protocol=" + url.getProtocol() + " port=" + url.getDefaultPort());
    }

    /** URL: короткий путь — openStream(). */
    static void urlDemo() throws Exception {
        // URL url = new URL("http://www.google.com");
        // InputStream is = url.openStream();       // просто читаем тело ответа
        // is.read();
        // is.close();
        //
        // Object o = url.getContent();             // определить тип содержимого
        //
        // Методы: getProtocol(), getHost(), getPort(), getPath(), getQuery(),
        //         getRef(), openConnection(), openStream().

        // В современном коде вместо new URL(...) предпочитают URI.create(...).toURL()
        // либо HttpClient (Java 11+), о котором — в отдельном курсе.
    }

    /** URLConnection: больше контроля над HTTP. */
    static void urlConnectionDemo() throws Exception {
        // URL url = new URL("http://example.com");
        // URLConnection uc = url.openConnection();
        // uc.connect();
        //
        // InputStream is = uc.getInputStream();    // читаем ответ
        // uc.setDoOutput(true);
        // OutputStream os = uc.getOutputStream();  // шлём данные (POST)
        // is.close(); os.close(); uc.close();
        //
        // Заголовки:
        //   uc.setRequestProperty("Content-Type", "application/json");
        //   uc.getHeaderField("Content-Type");
        //   uc.getHeaderFields();
        //
        // Кэш/таймауты:
        //   uc.setUseCaches(false);
        //   uc.setConnectTimeout(1000);
        //   uc.setReadTimeout(1000);
        //   uc.setRequestMethod("GET"); // только для HttpURLConnection
    }

    /** Шаблон Proxy: тот же интерфейс, делегирование реальному объекту. */
    static void proxyPatternDemo() {
        // Client → Subject ← RealSubject
        //                 ← Proxy (держит RealSubject)

        Subject subject = new Proxy(new RealSubject());
        System.out.println(subject.request());

        // Виды прокси:
        //   Virtual  — ленивое создание тяжёлого объекта;
        //   Remote   — прокси для удалённого объекта (идея лабы 6);
        //   Security — контроль доступа;
        //   Caching  — кэширование результатов;
        //   Smart    — логирование, рефкаунтинг и т. п.
    }

    /** Динамический прокси через java.lang.reflect.Proxy. */
    static void dynamicProxyDemo() {
        // Прокси создаётся «на лету» для любого интерфейса.
        Subject real = new RealSubject();
        InvocationHandler handler = (proxy, method, args) -> {
            System.out.println("before " + method.getName());
            Object result = method.invoke(real, args);
            System.out.println("after  " + method.getName());
            return result;
        };
        Subject dynamic = (Subject) java.lang.reflect.Proxy.newProxyInstance(
                Subject.class.getClassLoader(),
                new Class<?>[]{ Subject.class },
                handler);
        System.out.println(dynamic.request());
    }

    // --- Модель для паттерна Proxy --------------------------------------

    interface Subject {
        String request();
    }

    static class RealSubject implements Subject {
        @Override public String request() { return "real"; }
    }

    /** Классический прокси-объект с делегированием. */
    static class Proxy implements Subject {
        private final Subject real;
        Proxy(Subject real) { this.real = real; }
        @Override public String request() { return real.request(); }
    }
}
