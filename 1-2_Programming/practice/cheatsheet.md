# Java Cheatsheet

Шпоры по темам практики: типы данных и обобщения Java.

## Содержание

- [Java Cheatsheet](#java-cheatsheet)
  - [1. Типы данных и переменные](#1-типы-данных-и-переменные)
    - [Примитивные и ссылочные типы](#примитивные-и-ссылочные-типы)
    - [Классы-обёртки и преобразования](#классы-обёртки-и-преобразования)
    - [Символы Unicode](#символы-unicode)
  - [2. Обобщения (Generics)](#2-обобщения-generics)
    - [Обобщённый класс и метод](#обобщённый-класс-и-метод)
    - [Ограничения типов](#ограничения-типов)
    - [Wildcards и правило PECS](#wildcards-и-правило-pecs)
    - [Стирание типов](#стирание-типов)
  - [3. Компиляция и запуск](#3-компиляция-и-запуск)

---

## 1. Типы данных и переменные

Java — язык со **статической** и **строгой** типизацией: тип переменной известен при компиляции, а значение несовместимого типа присвоить нельзя.

```java
int count = 10;
String name = "Анна";
// count = name; // Ошибка компиляции
```

### Примитивные и ссылочные типы

| Группа | Типы | Что хранят |
| --- | --- | --- |
| Целые | `byte`, `short`, `int`, `long` | Целые числа |
| Дробные | `float`, `double` | Числа с дробной частью |
| Логический | `boolean` | `true` или `false` |
| Символьный | `char` | Одну UTF-16 кодовую единицу |
| Ссылочные | `String`, массивы, классы, интерфейсы | Ссылку на объект |

Суффиксы нужны, когда литерал имеет не тип `int` или `double`:

```java
long population = 8_100_000_000L;
float temperature = 23.5F;
double pi = 3.14159;
```

### Классы-обёртки и преобразования

У каждого примитива есть объектная обёртка: `Integer`, `Long`, `Double`, `Character`, `Boolean` и другие. Они необходимы, например, для коллекций и предоставляют полезные методы.

```java
Integer boxed = 42;       // автоупаковка: int -> Integer
int value = boxed;        // автораспаковка: Integer -> int
double d = boxed.doubleValue();

int year = Integer.parseInt("2026");
boolean ready = Boolean.parseBoolean("true");
```

Расширение диапазона выполняется неявно; сужение требует явного приведения и может потерять данные.

```java
int value = 300;
long safe = value;            // int -> long
byte overflow = (byte) value; // int -> byte, результат: 44
```

### Символы Unicode

`char` — не всегда «один символ, который видит человек»: он хранит только одну UTF-16 кодовую единицу. Символы вне базовой плоскости Unicode, например 😀 (`U+1F600`), представляются парой `char`.

```java
int codePoint = 0x1F600;
String emoji = new String(Character.toChars(codePoint));
System.out.println(emoji); // 😀
```

---

## 2. Обобщения (Generics)

**Generics** позволяют передавать тип как параметр. Это даёт проверку типов при компиляции, избавляет от лишних приведений и делает код переиспользуемым.

### Обобщённый класс и метод

По соглашению `T` — тип элемента, `K` — ключ, `V` — значение.

```java
class Box<T> {
    private T value;

    void put(T value) { this.value = value; }
    T get() { return value; }
}

Box<String> box = new Box<>();
box.put("Привет");
String text = box.get(); // приведение String не нужно

static <T> T getFirst(List<T> list) {
    return list.get(0);
}
```

### Ограничения типов

`extends` ограничивает допустимые типы. В Java оно применяется и к классам, и к интерфейсам.

```java
class NumberBox<T extends Number> {
    private final T value;

    NumberBox(T value) { this.value = value; }
    double asDouble() { return value.doubleValue(); }
}
```

Несколько ограничений записываются через `&`: `<T extends Number & Comparable<T>>`.

### Wildcards и правило PECS

`?` означает неизвестный тип.

| Запись | Значение | Когда использовать |
| --- | --- | --- |
| `List<?>` | список неизвестного типа | важен только факт, что это список |
| `List<? extends Number>` | `Number` или его потомки | читать значения как `Number` |
| `List<? super Integer>` | `Integer` или его предки | добавлять `Integer` |

**PECS**: *Producer Extends, Consumer Super* — источник данных использует `extends`, получатель данных использует `super`.

```java
static double sum(List<? extends Number> values) {
    double total = 0;
    for (Number value : values) total += value.doubleValue();
    return total;
}

static <T> void copy(List<? super T> target, List<? extends T> source) {
    target.addAll(source);
}
```

### Стирание типов

Параметры типов существуют в основном во время компиляции. После неё Java стирает информацию о `T` (*type erasure*), поэтому нельзя:

- создавать `new T()` и `new T[10]`;
- проверять `object instanceof Box<String>`;
- объявлять статическое поле типа `T`;
- перегружать методы, которые после стирания имеют одну сигнатуру.

---

## 3. Компиляция и запуск

Из папки конкретного урока `java`:

```powershell
javac -d bin src/App.java
java -cp bin App
```

`javac` компилирует исходник в папку `bin`, а `java` запускает класс `App` из неё.
