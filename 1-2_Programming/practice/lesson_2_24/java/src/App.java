import javax.naming.*;
import javax.sql.*;
import javax.sql.rowset.*;
import java.sql.*;

/**
 * Урок 2.24 — расширения JDBC.
 */
public class App {

    public static void main(String[] args) throws Exception {
        dataSource();
        jndiDemo();
        rowSets();
    }

    /** DataSource — фабрика соединений. */
    static void dataSource() throws Exception {
        // javax.sql.DataSource — альтернатива DriverManager.
        //   Connection getConnection() / getConnection(user, pass)
        //   org.postgresql.ds.PGSimpleDataSource — простая реализация.
        //
        // Разновидности:
        //   ConnectionPoolDataSource — пул соединений (PGPoolingDataSource);
        //   XADataSource             — распределённые транзакции.

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerName("db");
        ds.setDatabaseName("studs");
        ds.setUser("u");
        ds.setPassword("p");
        try (Connection c = ds.getConnection()) {
            // ...
        }
        // Импорт: org.postgresql.ds.PGSimpleDataSource из драйвера postgresql.jar.
    }

    /** JNDI — регистрация DataSource в контексте имён. */
    static void jndiDemo() throws Exception {
        // javax.naming.Context / InitialContext.
        // Идея: приложение не знает параметры подключения — они достаются
        // из каталога по имени.

        // Регистрация:
        // Context ctx = new InitialContext();
        // ctx.bind("testDB", ds);

        // Получение:
        Context ctx = new InitialContext();
        // DataSource ds = (DataSource) ctx.lookup("testDB");
        // Connection c = ds.getConnection();
    }

    /** RowSet — интерфейс ResultSet с настройками и без постоянного соединения. */
    static void rowSets() throws Exception {
        // javax.sql.rowset.RowSet extends ResultSet.
        // Умеет сам подключаться: setUrl/setUsername/setPassword/setCommand,
        // execute(), next(), getXxx().
        RowSetFactory factory = RowSetProvider.newFactory();

        // 1) JdbcRowSet — «тонкая» обёртка над ResultSet, держит соединение.
        //    По умолчанию TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATEABLE.
        JdbcRowSet jrs = factory.createJdbcRowSet();
        jrs.setUrl("jdbc:postgresql://db:5432/studs");
        jrs.setUsername("u");
        jrs.setPassword("p");
        jrs.setCommand("SELECT * FROM users");
        jrs.execute();
        jrs.last();
        jrs.getInt("id");
        jrs.updateString("name", "Pupkin");
        jrs.updateRow();

        // 2) CachedRowSet — отсоединённый RowSet, результат кэшируется.
        //    Синхронизация с БД: acceptChanges(); конфликты решаются отдельно.
        CachedRowSet crs = factory.createCachedRowSet();
        crs.setCommand("SELECT * FROM users");
        // ... execute(), изменить строки, затем:
        // crs.acceptChanges(connection);

        // 3) WebRowSet — CachedRowSet + XML.
        //    writeXML(writer) / readXML(reader).
        WebRowSet wrs = factory.createWebRowSet();
        // wrs.writeXML(new FileWriter("data.xml"));

        // 4) FilteredRowSet — аналог WHERE на уже выбранных строках.
        //    setFilter(Predicate); Predicate умеет evaluate(value, col),
        //    evaluate(RowSet).
        FilteredRowSet frs = factory.createFilteredRowSet();
        // frs.setFilter(pred);

        // 5) JoinRowSet — соединение нескольких RowSet.
        //    addRowSet(rs, matchColumn...), setJoinType(...), toCachedRowSet().
        JoinRowSet join = factory.createJoinRowSet();
        // users.setMatchColumn("uid"); groups.setMatchColumn("uid");
        // join.addRowSet(users); join.addRowSet(groups);
    }
}
