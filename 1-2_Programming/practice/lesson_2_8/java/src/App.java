import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Урок 2.8 — стандартные потоки и Scanner.
 */
public class App {

    public static void main(String[] args) throws Exception {
        standardStreams();
        console();
        scannerBasics();
        scannerDelimiters();
        scannerMultiline();
    }

    /** System.in / System.out / System.err — стандартные потоки. */
    static void standardStreams() {
        // System.in  — InputStream  (байты со стандартного ввода)
        // System.out — PrintStream  (стандартный вывод)
        // System.err — PrintStream  (стандартный вывод ошибок)
        // Все три — статические поля класса System.
        // Можно заменить: System.setIn(...), setOut(...), setErr(...).

        // System.out переопределяется для тестов/логирования:
        //   PrintStream ps = new PrintStream(new FileOutputStream("out.txt"), true);
        //   System.setOut(ps);

        // Потоки не закрываем вручную: System.in/out/err управляются JVM.
    }

    /** java.io.Console — удобный ввод/вывод с терминала. */
    static void console() {
        Console c = System.console();
        if (c == null) {
            // Console == null, если нет интерактивной консоли (IDE, пайп).
            return;
        }
        // Методы:
        //   String readLine()
        //   String readLine(String fmt, Object... args)
        //   char[] readPassword()
        //   char[] readPassword(String fmt, Object... args)
        //   PrintWriter writer()
        //   Console printf(String, Object...) / format(...)
        //   Console flush()
        // Преимущество readPassword(): пароль не появляется в echo.
    }

    /** Scanner — базовое чтение из System.in. */
    static void scannerBasics() {
        Scanner sc = new Scanner(System.in);

        // Методы:
        //   boolean hasNext() / boolean hasNextInt()
        //   String next() / String nextLine()
        //   int nextInt() / long / short / byte / float / double / boolean / char
        //   Scanner useDelimiter(String/Pattern)
        //   Scanner useRadix(int)
        //   void close()

        // Пример: читаем одно число и строку.
        // int n = sc.nextInt();
        // String s = sc.nextLine();
        // Обязательно закрываем — иначе ресурс висит.
        // sc.close();
    }

    /** Scanner: разделитель и система счисления. */
    static void scannerDelimiters() {
        // useDelimiter(";\\s*") — разделитель — точка с запятой + пробелы.
        // useRadix(16) — читаем числа как шестнадцатеричные.
        // Работает поверх того же потока, что и Scanner(System.in).
        try (Scanner sc = new Scanner("a;b;c; d").useDelimiter(";\\s*")) {
            while (sc.hasNext()) System.out.print(sc.next() + " ");
            System.out.println();
        }
    }

    /** Scanner по строкам: вложенный Scanner на каждой строке. */
    static void scannerMultiline() {
        var source = "100 200 300 400 500\n1 2 3 4 5\n-1 -2 -3 -4 -5";
        List<List<Integer>> ints = new ArrayList<>();

        try (Scanner scanner = new Scanner(source)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try (Scanner intScanner = new Scanner(line)) {
                    List<Integer> row = new ArrayList<>();
                    while (intScanner.hasNextInt()) row.add(intScanner.nextInt());
                    ints.add(row);
                }
            }
        }
        System.out.println(ints);
    }
}
