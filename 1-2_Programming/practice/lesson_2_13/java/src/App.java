import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Урок 2.13 — сетевое взаимодействие: теория.
 */
public class App {

    public static void main(String[] args) throws Exception {
        architecture();
        stack();
        addresses();
        portsAndSockets();
    }

    /** Архитектуры сетей. */
    static void architecture() {
        // Вычислительная сеть — система передачи данных между узлами.
        // Хост — компьютер, подключённый к сети и имеющий сетевой адрес.
        // Протокол — набор правил: порядок действий и формат данных.

        // Клиент-сервер:
        //   - централизованное управление;
        //   - сервер ждёт запрос, клиент шлёт запрос и ждёт ответ;
        //   - сервер — критический узел, надёжность зависит от него.
        //
        // Peer-to-peer (одноранговая):
        //   - децентрализованное управление;
        //   - все узлы равноправны, каждый может быть и клиентом, и сервером;
        //   - нет критического узла.
    }

    /** Модель ISO/OSI и стек TCP/IP. */
    static void stack() {
        // TCP/IP          OSI                  Пример
        // ----------------------------------------------
        // Прикладной      Прикладной           HTTP
        //                 Представительский
        //                 Сеансовый
        // Транспортный    Транспортный         TCP, UDP
        // Сетевой         Сетевой              IP
        // Канальный       Канальный            Ethernet
        //                 Физический           витая пара
        //
        // Инкапсуляция: каждый уровень оборачивает данные заголовком (пакет = header + data).
        // При приёме — обратный процесс: снятие заголовков снизу вверх.

        // Прикладной уровень: HTTP, FTP, SMTP, DNS.
        // Транспортный уровень:
        //   TCP — соединение, подтверждение доставки, надёжность;
        //   UDP — без соединения и подтверждения, скорость.
    }

    /** IP-адреса, IPv4 / IPv6, DNS, InetAddress. */
    static void addresses() throws UnknownHostException {
        // IP-адрес идентифицирует связь между роутером и хостом.
        // ID сети (префикс) + ID хоста (суффикс).
        //
        // IPv4 — 32 бита, 194.85.160.55
        //   Класс A: 0...        | 8 бит сети + 24 бита хоста
        //   Класс B: 10...       | 16 + 16
        //   Класс C: 110...      | 24 + 8
        //   Маска подсети: 192.168.0.5/255.255.255.240 или /28
        //
        // IPv6 — 128 бит, [FC05::4429:0:AC02]
        // Loopback: 127.0.0.1 / [::1] (localhost)

        // DNS (Domain Name Service): имя ↔ IP.
        // www.google.com ↔ 172.217.23.132

        // InetAddress — статические фабрики:
        InetAddress local = InetAddress.getLocalHost();      // текущий хост
        InetAddress byName = InetAddress.getByName("google.com"); // обращение к DNS
        InetAddress byAddr = InetAddress.getByAddress(
                new byte[]{ (byte) 8, (byte) 8, (byte) 8, (byte) 8 }); // без DNS

        // Нестатические методы:
        byte[] raw = byName.getAddress();                    // 4 или 16 байт
        String name = byName.getHostName();                  // имя хоста

        System.out.println(local + " / " + byName + " / " + byAddr);
        System.out.println("raw=" + raw.length + " host=" + name);

        // Специализации: Inet4Address, Inet6Address — наследуют InetAddress.
    }

    /** Порты, сокеты, InetSocketAddress. */
    static void portsAndSockets() {
        // IP-адрес идентифицирует хост.
        // Порт идентифицирует процесс (приложение) на хосте.
        // Сокет = IP + порт — интерфейс обмена.
        //
        // Для обмена нужно знать:
        //   - протокол (TCP / UDP);
        //   - IP-адрес и порт отправителя;
        //   - IP-адрес и порт получателя.
        //
        // Сервер:
        //   - работает на известном хосте с известным IP;
        //   - слушает известный порт (80 для HTTP, 443 для HTTPS, 25 для SMTP);
        //   - ждёт запрос, отвечает.
        // Клиент:
        //   - работает на любом хосте (сервер не знает, где);
        //   - выбирает свободный порт при отправке;
        //   - посылает запрос, ждёт ответ.

        // InetSocketAddress — адрес + порт.
        var a1 = new InetSocketAddress(80);                       // wildcard-адрес + порт
        var a2 = new InetSocketAddress("example.com", 443);       // имя хоста + порт
        System.out.println(a1 + " / " + a2);

        // Результат разрешения имени доступен через методы:
        // a2.getHostName(), a2.getAddress(), a2.getPort().
    }
}
