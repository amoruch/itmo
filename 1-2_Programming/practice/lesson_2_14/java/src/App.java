import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Урок 2.14 — сетевой обмен: UDP и TCP (классический IO).
 */
public class App {

    public static void main(String[] args) throws Exception {
        // В одном процессе запускаем сервер и клиент по очереди,
        // чтобы пример был самодостаточным.
        runUdpServerOnce();
        runUdpClient();

        runTcpServerOnce();
        runTcpClient();
    }

    // --- UDP ------------------------------------------------------------------

    /** UDP-сервер: слушает порт, получает датаграмму, отправляет ответ. */
    static void runUdpServerOnce() throws Exception {
        int port = 6789;
        byte[] arr = new byte[10];

        try (DatagramSocket ds = new DatagramSocket(port)) {
            // DatagramPacket — «конверт»: буфер + адрес/порт.
            // При получении в пакет кладём буфер и его длину.
            DatagramPacket dp = new DatagramPacket(arr, arr.length);
            ds.receive(dp); // блокирующее ожидание датаграммы

            // Обрабатываем данные, полученные от клиента.
            for (int j = 0; j < arr.length; j++) arr[j] *= 2;

            // Отправляем ответ обратно на адрес/порт отправителя.
            dp.setData(arr, 0, arr.length);
            ds.send(dp);
        }
    }

    /** UDP-клиент: отправляет датаграмму, ждёт ответ. */
    static void runUdpClient() throws Exception {
        int port = 6789;
        byte[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        InetAddress host = InetAddress.getByName("localhost");

        try (DatagramSocket ds = new DatagramSocket()) { // порт выбирается ОС
            // Пакет с адресом получателя — для отправки.
            DatagramPacket dp = new DatagramPacket(arr, arr.length, host, port);
            ds.send(dp);

            // Тот же буфер переиспользуем для приёма ответа.
            dp = new DatagramPacket(arr, arr.length);
            ds.receive(dp);

            for (byte b : arr) System.out.print(b + " ");
            System.out.println();
        }

        // Особенности UDP:
        //   - без установления соединения, без подтверждения доставки;
        //   - датаграмма ≤ ~64 Кбайт (реально меньше по MTU);
        //   - может теряться, дублироваться, приходить в другом порядке;
        //   - быстрее TCP (нет накладных расходов).
    }

    // --- TCP ------------------------------------------------------------------

    /** TCP-сервер: ServerSocket.accept() + потоки ввода-вывода. */
    static void runTcpServerOnce() throws Exception {
        int port = 6789;
        byte[] arr = new byte[10];

        try (ServerSocket serv = new ServerSocket(port);
             Socket sock = serv.accept()) {          // блокирует до подключения клиента

            // Обмен через потоки, как с обычным IO.
            try (InputStream is = sock.getInputStream();
                 OutputStream os = sock.getOutputStream()) {

                is.read(arr);                        // читаем массив
                for (int j = 0; j < arr.length; j++) arr[j] *= 2;
                os.write(arr);                       // отправляем обратно
                os.flush();
            }
        }
    }

    /** TCP-клиент: Socket(host, port) + потоки. */
    static void runTcpClient() throws Exception {
        int port = 6789;
        byte[] arr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        InetAddress host = InetAddress.getByName("localhost");

        try (Socket sock = new Socket(host, port);
             OutputStream os = sock.getOutputStream();
             InputStream  is = sock.getInputStream()) {

            os.write(arr);
            os.flush();                              // обязательно для TCP
            is.read(arr);                            // дождаться ответа
            for (byte b : arr) System.out.print(b + " ");
            System.out.println();
        }

        // Особенности TCP:
        //   - соединение устанавливается (three-way handshake);
        //   - подтверждение доставки, порядок сохраняется;
        //   - двунаправленный обмен через InputStream / OutputStream;
        //   - надёжнее UDP, но медленнее.
    }
}
