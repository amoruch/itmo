# Практика по программированию

Практика ведётся по лекционным презентациям. Для каждой лекции код остаётся в привычной папке `lesson_*`: комментарии объясняют только сам пример, а сжатая теория и команды постепенно собираются в [cheatsheet.md](cheatsheet.md).

## Уже переработано

| Папка | Лекция |
| --- | --- |
| [`lesson_1_1/java`](lesson_1_1/java) | Введение, типы и выражения |
| [`lesson_1_2/java`](lesson_1_2/java) | Ветвления, массивы, циклы и методы |
| [`lesson_1_3/java`](lesson_1_3/java) | Основы ООП: классы, объекты и инкапсуляция |
| [`lesson_1_4/java`](lesson_1_4/java) | Наследование, полиморфизм, `Object`, `enum` и `record` |
| [`lesson_1_5/java`](lesson_1_5/java) | Интерфейсы, абстрактные классы, коллекции и стандартная библиотека |
| [`lesson_1_6/java`](lesson_1_6/java) | Исключения, проектирование, чистый код и частые ловушки Java |
| [`lesson_1_7/java`](lesson_1_7/java) | Пакеты, модули, Javadoc, тестирование, логирование и Git |

Остальные уроки сохранены в исходном виде и будут перерабатываться последовательно, по одной лекции за раз.

## Запуск Java-примера

В папке конкретного урока `java`:

```powershell
javac -encoding UTF-8 -d bin src/*.java
java -cp bin App
```

Для примера с JPMS-модулем (`lesson_1_7`):

```powershell
javac -encoding UTF-8 -d bin src/module-info.java src/itmo/practice/app/*.java
java --module-path bin --module itmo.practice/itmo.practice.app.App
```