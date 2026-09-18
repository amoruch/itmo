import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

/**
 * Урок 2.23 — базы данных и JDBC.
 */
public class App {

    public static void main(String[] args) throws Exception {
        dbTheory();
        sqlBasics();
        jdbcConnection();
        statements();
        resultSetDemo();
        transactions();
        metadataDemo();
    }

    /** БД, СУБД, реляционная модель. */
    static void dbTheory() {
        // БД — структурированные данные + информация о данных и связях.
        // СУБД — система для создания и использования БД.
        // Реляционная модель: отношение = таблица, атрибуты = столбцы,
        // кортежи = строки. У атрибута есть тип; атрибуты и кортежи
        // не повторяются, их порядок не важен.
    }

    /** SQL: DDL / DML / SELECT. */
    static void sqlBasics() {
        // DDL:
        //   CREATE TABLE weather (city VARCHAR(80), temp_lo INT, temp_hi INT,
        //                         prcp REAL, date DATE);
        //   DROP TABLE weather;
        // Ограничения: типы (INT, VARCHAR(n), DATE, ...), NOT NULL, UNIQUE,
        //   CHECK (age >= 18), PRIMARY KEY, FOREIGN KEY.
        //
        // DML:
        //   INSERT INTO weather VALUES ('Oslo', 46, 50, 0.25, '2021-11-27');
        //   UPDATE weather SET temp_hi = temp_hi - 2 WHERE date > '2021-11-28';
        //   DELETE FROM weather WHERE city = 'Oslo';
        //   COPY persons TO file; / COPY persons FROM file;
        //
        // SELECT:
        //   SELECT * FROM students;
        //   WHERE / ORDER BY / DISTINCT / COUNT(*);
        //   JOIN groups ON students.group = groups.group;
    }

    /** Подключение через DriverManager. */
    static void jdbcConnection() throws Exception {
        // JDBC — единый API + драйвер под каждую СУБД. Пакеты java.sql / javax.sql.
        // Типы драйверов: 1) мост ODBC, 2) DB API, 3) middleware, 4) pure Java.
        // Загрузка драйвера: Class.forName(...), jdbc.drivers,
        //   либо ServiceLoader (META-INF/services/java.sql.Driver).
        //
        // URL: jdbc:protocol://host:port/database
        //      jdbc:postgresql://localhost:5432/studs

        String url = "jdbc:postgresql://localhost:5432/studs";

        // Прямо:
        // Connection c = DriverManager.getConnection(url, "user", "pass");

        // Через Properties из файла:
        //   db.cfg:
        //     database.url=jdbc:postgresql://localhost:5432/studs
        //     database.username=test
        //     database.password=password
        Properties info = new Properties();
        info.load(new FileInputStream("db.cfg"));
        // Connection c = DriverManager.getConnection(url, info);

        // Рекомендуется try-with-resources: Connection — AutoCloseable.
    }

    /** Connection и семейство Statement. */
    static void statements() throws Exception {
        try (Connection c = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/studs", "user", "pass")) {

            // Statement — статический запрос.
            try (Statement st = c.createStatement()) {
                st.executeQuery("SELECT * FROM students");
            }

            // PreparedStatement — с параметрами (?), защита от SQL-инъекций.
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT * FROM students WHERE id = ?")) {
                ps.setInt(1, 15);
                ps.executeQuery();
            }

            // CallableStatement — вызов хранимой процедуры.
            try (CallableStatement cs = c.prepareCall("CALL getResult(?)")) {
                cs.setInt(1, 15);
                cs.registerOutParameter(1, Types.INTEGER);
                cs.execute();
                int result = cs.getInt(1);
            }

            // Методы выполнения:
            //   executeQuery()  → ResultSet     (SELECT)
            //   executeUpdate() → int           (INSERT/UPDATE/DELETE/DDL)
            //   execute()       → boolean; далее getResultSet() / getUpdateCount()
        }
    }

    /** ResultSet: навигация, получение, обновление, метаданные. */
    static void resultSetDemo() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "u", "p");
             PreparedStatement ps = c.prepareStatement("SELECT * FROM students");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);                   // по номеру столбца
                String name = rs.getString("name");      // по имени
                // getBoolean / getLong / getDouble / getDate / getTimestamp / getArray
            }

            // Навигация (для scrollable): next / previous / first / last /
            //   beforeFirst / afterLast / absolute(n) / relative(n) / moveToInsertRow.
            //
            // Тип/конкурентность/holdability задаются в createStatement(sql, type, ...):
            //   TYPE_FORWARD_ONLY / TYPE_SCROLL_INSENSITIVE / TYPE_SCROLL_SENSITIVE;
            //   CONCUR_READ_ONLY / CONCUR_UPDATEABLE;
            //   HOLD_CURSORS_OVER_COMMIT / CLOSE_CURSORS_AT_COMMIT.
            //
            // Обновление/вставка (для updatable):
            //   rs.updateString("name", "Pupkin"); rs.updateRow();
            //   rs.moveToInsertRow(); rs.updateInt(...); rs.insertRow();
            //
            // Метаданные результата:
            ResultSetMetaData md = rs.getMetaData();
            md.getColumnCount();
            md.getColumnName(1);
            md.getColumnType(1);
            md.getTableName(1);                            // полезно при JOIN
        }
    }

    /** Транзакции и пакетная обработка. */
    static void transactions() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "u", "p")) {
            c.setAutoCommit(false);
            try (Statement st = c.createStatement()) {
                st.executeUpdate("UPDATE accounts SET bal = bal - 100 WHERE id = 1");
                st.executeUpdate("UPDATE accounts SET bal = bal + 100 WHERE id = 2");
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }

            // Savepoint:
            // Savepoint sp = c.setSavepoint("mid"); c.rollback(sp);

            // Пакетная обработка:
            try (Statement st = c.createStatement()) {
                st.addBatch("INSERT INTO t VALUES (1)");
                st.addBatch("INSERT INTO t VALUES (2)");
                int[] results = st.executeBatch();
                st.clearBatch();
            }
        }
    }

    /** Метаданные соединения. */
    static void metadataDemo() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "u", "p")) {
            DatabaseMetaData dmd = c.getMetaData();
            dmd.getCatalogs();
            dmd.getSchemas();
            // getTables(catalog, schemaPattern, tableNamePattern, types)
            // null означает «любой».
            dmd.getTables(null, null, null, null);
        }
    }
}
