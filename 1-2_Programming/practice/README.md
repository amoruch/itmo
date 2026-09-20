# Практика по программированию

Практика ведётся по лекционным презентациям. Для каждой лекции код остаётся в привычной папке `lesson_*`: комментарии объясняют только сам пример, а сжатая теория и команды постепенно собираются в [cheatsheet.md](cheatsheet.md).

## Уже переработано

| Папка                                | Лекция                                                                |
| ------------------------------------ | --------------------------------------------------------------------- |
| [`lesson_1_1/java`](lesson_1_1/java) | Введение, типы и выражения                                            |
| [`lesson_1_2/java`](lesson_1_2/java) | Ветвления, массивы, циклы и методы                                    |
| [`lesson_1_3/java`](lesson_1_3/java) | Основы ООП: классы, объекты и инкапсуляция                            |
| [`lesson_1_4/java`](lesson_1_4/java) | Наследование, полиморфизм, `Object`, `enum` и `record`                |
| [`lesson_1_5/java`](lesson_1_5/java) | Интерфейсы, абстрактные классы, коллекции и стандартная библиотека    |
| [`lesson_1_6/java`](lesson_1_6/java) | Исключения, проектирование, чистый код и частые ловушки Java          |
| [`lesson_1_7/java`](lesson_1_7/java) | Пакеты, модули, Javadoc, тестирование, логирование и Git              |
| [`lesson_2_1/java`](lesson_2_1/java) | Типы, generics и Collections Framework                                |
| [`lesson_2_2/java`](lesson_2_2/java) | Iterator, Builder, сортировка и ввод-вывод                            |
| [lesson_2_3/java](lesson_2_3/java)   | NIO, сеть, дата-время и лямбды                                        |
| [lesson_2_4/java](lesson_2_4/java)   | Functional interfaces, Stream API, JDBC и безопасность                |
| [`lesson_2_5/java`](lesson_2_5/java) | Многопоточность: потоки, синхронизация, пулы и `java.util.concurrent` |
| [`lesson_2_6/javafx`](lesson_2_6/javafx) | GUI: Swing, Java 2D, JavaFX и локализация                           |
| [`lesson_2_7/java`](lesson_2_7/java) | Шаблоны проектирования и безопасный код — в процессе                  |
| [`lesson_2_8/java`](lesson_2_8/java) | Reflection, Method Handle, VarHandle, аннотации и инструменты         |

В `lesson_2_7` реализована только часть паттернов; к остальным вернёмся отдельно.

## Запуск Java-примера

В папке конкретного урока `java`:

```powershell
javac -encoding UTF-8 -d bin src/*.java
java -cp bin App
```

Для JavaFX-примера (`lesson_2_6`) нужен Maven:

```powershell
cd lesson_2_6\javafx
mvn javafx:run
```

Для примера с JPMS-модулем (`lesson_1_7`):

```powershell
javac -encoding UTF-8 -d bin src/module-info.java src/itmo/practice/app/*.java
java --module-path bin --module itmo.practice/itmo.practice.app.App
```