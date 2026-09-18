/**
 * Урок 1.7 — модули и инструменты разработки.
 */
public class App {

    public static void main(String[] args) {
        // Пакеты: логические группы классов, пространство имён, обратный домен.
        // package в первой строке файла, каталог = имя пакета.
        // import — короткое имя класса; import static — статические члены.
        // java.lang импортируется автоматически.

        // Classpath: каталоги и JAR'ы, где искать классы.
        //   javac -cp lib/classes Hello.java
        //   java  -cp lib/classes:app.jar mypackage.Main
        // JAR = zip + META-INF/MANIFEST.MF (Main-Class для исполняемого).
        // JAR Hell: используется первый найденный класс — нужен контроль версий.

        // JPMS (Java 9+): module-info.java — требует requires/exports/opens.
        //   module my.app {
        //       requires java.sql;
        //       requires transitive lib;
        //       requires static compile.only;
        //       exports pkg.api;
        //       exports pkg.internal to other.module;
        //       opens   pkg.reflect to other.module;
        //   }
        // Виды модулей:
        //   named    — с module-info, полный контроль, не видят безымянных;
        //   automatic— JAR без module-info на module-path, видит всех и всё отдаёт;
        //   unnamed  — всё, что на class-path, для обратной совместимости.

        // Сборка и зависимости: Maven (pom.xml), Gradle (build.gradle).
        // Внешние версии — в build-файле; внутренние связи — в module-info.

        // --- Документация Javadoc ---
        // /** ... */ перед классом/методом; первое предложение — summary.
        // Теги класса:  @author, @version, @since, @see, @deprecated
        // Теги метода:  @param, @return, @throws
        // Разметка:     {@link X#m()}, {@code List<String>}, {@literal < >}
        // package-info.java — документация пакета.
        // Генерация: javadoc -d doc -author -version *.java
        // Описываем «что?» и «зачем?», а не «как?».

        // --- Тестирование ---
        // assert — инвариант в коде, включается -ea, только для разработки:
        assert args != null : "args never null";
        // Unit-тест — код, проверяющий код. AAA: Arrange / Act / Assert.
        // JUnit: @Test, assertEquals, assertTrue/False, assertThrows, assertArrayEquals.
        // Хорошие тесты: FIRST — Fast, Isolated, Repeatable, Self-Validating, Timely.
        // TDD: сначала падающий тест, потом код.

        // --- Логирование ---
        // Logger -> Filter -> Handler -> Formatter -> вывод.
        // Уровни: ERROR/WARN/INFO/CONFIG/FINE/ALL/OFF.
        var log = java.util.logging.Logger.getLogger(App.class.getName());
        log.info("hello");
        // Не println для диагностики — используем логгеры (java.util.logging, log4j, logback).

        // --- Отладка ---
        // Breakpoints, step in/over/out, watch, conditional breakpoints.

        // --- Git ---
        // git init / clone
        // git add <file>      — в staging
        // git commit -m "..." — фиксация версии
        // git push / pull     — синхронизация
        // git status / log / branch / switch / merge
        // Частые маленькие коммиты; один коммит — одно изменение;
        // осмысленные сообщения; ветки под фичи; .gitignore для секретов.
    }
}
