/**
 * Урок 2.26 — модули (JPMS, Java 9+).
 *
 * Демонстрация идёт как комментарии — сам module-info.java
 * не компилируется вне модульного проекта.
 */
public class App {

    public static void main(String[] args) {
        whyModules();
        moduleInfoSyntax();
        moduleTypes();
        tooling();
    }

    /** Проблемы, которые решают модули. */
    static void whyModules() {
        // До Java 9: классы → пакеты → JAR-архивы.
        // Пакет ≈ каталог; JAR — ZIP с классами и META-INF/MANIFEST.MF.
        //
        // Проблемы больших приложений:
        //   - JAR Hell: ищется первый подходящий класс на classpath;
        //     конфликты версий, дубликаты классов, «не та» версия;
        //   - нет контроля зависимостей на уровне JVM;
        //   - public-классы видны всем;
        //   - монолитный rt.jar со всей стандартной библиотекой.
        //
        // JPMS (Java Platform Module System) решает это явными
        // зависимостями и контролем доступа.
    }

    /** module-info.java — декларация модуля. */
    static void moduleInfoSyntax() {
        // Модуль — группа пакетов + директивы доступа.
        // Файл module-info.java лежит в корне модуля.

        // module my.mod {
        //     requires java.base;                    // всегда есть неявно
        //     requires java.sql;
        //     requires transitive lib;               // и всем, кто depends on my.mod
        //     requires static compile.only;          // только на этапе компиляции
        //
        //     exports my.package;                    // виден всем (компиляция + запуск)
        //     exports my.internal to other.mod;      // только указанному модулю
        //
        //     opens my.reflect;                      // для рефлексии во время выполнения
        //     opens my.reflect to other.mod;
        //
        //     uses spi.Service;                      // использовать через ServiceLoader
        //     provides spi.Service with spi.Impl;    // предоставить свою реализацию
        // }
    }

    /** Типы модулей. */
    static void moduleTypes() {
        // Named (именованный):
        //   на module-path, с module-info.class;
        //   полный контроль доступа и зависимостей;
        //   НЕ видит код из безымянного модуля.
        //
        // Automatic (автоматический):
        //   JAR без module-info на module-path;
        //   имя = имя JAR без версии;
        //   видит всё и отдаёт всё — переходный режим миграции.
        //
        // Unnamed (безымянный):
        //   всё, что на class-path;
        //   авто-экспортирует свои пакеты, видит всех — обратная совместимость.
    }

    /** Инструменты и запуск. */
    static void tooling() {
        // Компиляция:
        //   javac --module-path mods:libs -d out $(find src -name '*.java')
        //
        // Запуск:
        //   java --module-path mods:libs --module my.mod/com.pkg.Main
        //
        // Сборка модульного JAR:
        //   jar --create --file app.jar --main-class com.pkg.Main -C out .
        //
        // jmod — новый формат модулей (для платформенных модулей).
        // jlink — сборка кастомного образа JRE только с нужными модулями:
        //   jlink --module-path $JAVA_HOME/jmods:mods --add-modules my.mod
        //         --output myjre
        //
        // В Java 11 rt.jar удалён; базовый модуль — java.base.
    }
}
