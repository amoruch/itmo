import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Урок 2.27 — безопасное хранение паролей.
 */
public class App {

    public static void main(String[] args) throws Exception {
        sqlInjectionProblem();
        hashingBasics();
        saltAndPepper();
        messageDigestDemo();
        externalSecrets();
    }

    /** Проблема: пароли в открытом виде и SQL-инъекции. */
    static void sqlInjectionProblem() {
        // Плохо: CREATE TABLE users (name VARCHAR, password VARCHAR);
        // Хранить пароль в открытом виде нельзя:
        //   - утечка БД → утечка паролей;
        //   - одинаковые пароли у разных людей видны;
        //   - резервные копии, дампы, логи;
        //   - SQL-инъекции и другие атаки.

        // SQL-инъекция через конкатенацию строк:
        //   q = "SELECT * FROM users WHERE name = '%s' AND password = '%s';"
        //   name = "Simon'; --"
        //   password = "1"
        //   → "SELECT * FROM users WHERE name = 'Simon'; -- ' AND password = '1';"
        //   → логин без пароля.
        //
        //   name = "' OR '='"
        //   password = "' OR '='"
        //   → WHERE ''='' AND ''='' → всегда истина.

        //   name = "' UNION SELECT name,password FROM users WHERE name<>'"
        //   → вытаскиваем чужие логины и пароли.

        // Защита:
        //   1) PreparedStatement с параметрами (?) — экранирование делает драйвер;
        //   2) никогда не конкатенировать пользовательские данные в SQL;
        //   3) ограничивать права пользователя БД.
    }

    /** Хеширование паролей. */
    static void hashingBasics() {
        // Храним не пароль, а его хеш: необратимое преобразование
        // с минимальным числом коллизий.
        //
        // Алгоритмы: MD2, MD5, SHA-1, SHA-224/256/384/512.
        //   md5("hello") = 5d41402abc4b2a76b9719d911017c592
        //
        // Проблемы:
        //   - «необратимо», но можно подобрать по словарю
        //     (md5.gromweb.com, reversemd5.com);
        //   - одинаковые пароли → одинаковые хеши:
        //     радужные таблицы, словарные атаки;
        //   - коллизии возможны (парадокс дней рождения: > 23 человек
        //     в группе → высокая вероятность совпадения дня рождения).

        // Поэтому: соль (salt) + перец (pepper).
    }

    /** Соль и перец. */
    static void saltAndPepper() {
        // Соль — случайная строка, уникальная для каждого пользователя.
        //   Хранится в БД рядом с хешем.
        //   md5("hello" + "$lns50D") ≠ md5("hello" + "HOxc3@")
        //   → одинаковые пароли дают разные хеши, словари бесполезны.
        //
        // В БД храним: hash + salt + алгоритм + сложность (cost / iterations).
        //
        // Перец — общий секрет для всего приложения, хранится ОТДЕЛЬНО от БД
        // (в конфиге с ограниченным доступом, переменной окружения и т. п.).
        //   md5("hello" + "kFz<Q%ps" + "$lns50D")
        //   → даже при утечке БД без перца хеши не восстановить.
        //
        // В реальных проектах вместо «сырых» MD5/SHA-1 используют
        // специализированные функции для паролей: bcrypt, scrypt, Argon2
        // (медленные, с параметром cost).

        // Хранение исходящих паролей (к БД, внешним сервисам) — отдельно от кода:
        //   - запрашивать при старте приложения;
        //   - конфиг-файл с ограниченными правами;
        //   - файл Properties;
        //   - переменная окружения;
        //   - ~/.pgpass (для PostgreSQL).
    }

    /** MessageDigest: считаем хеш пароля + соль + перец. */
    static void messageDigestDemo() throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        String password = "hello";
        String salt     = randomSalt();
        String pepper   = "*63&^mVLC(#";                       // хранится отдельно

        byte[] hash = md.digest((password + pepper + salt).getBytes("UTF-8"));
        String hex = HexFormat.of().formatHex(hash);

        System.out.println("salt=" + salt + " hash=" + hex);

        // Сохраняем в БД:
        //   INSERT INTO users(name, salt, hash) VALUES (?, ?, ?);
        // При логине повторяем: hash(password + pepper + salt) и сравниваем.
    }

    static String randomSalt() {
        byte[] b = new byte[8];
        new SecureRandom().nextBytes(b);
        return HexFormat.of().formatHex(b);
    }

    /** Исходящие пароли — отдельно от кода. */
    static void externalSecrets() {
        // Нельзя:  password = "LHoi>9158" прямо в App.java.
        // Можно:
        //   - Properties + Files.load(...) / getProperty(...);
        //   - переменные окружения (System.getenv("DB_PASSWORD"));
        //   - секретные хранилища (Vault, KeyStore, AWS Secrets Manager);
        //   - .pgpass с правами 600 (только для владельца).
        //
        // .gitignore для файлов с паролями — обязателен.
    }
}
