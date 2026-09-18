import java.util.*;

/**
 * Урок 2.2.5 — Collections Framework.
 */
public class App {

    public static void main(String[] args) {
        hierarchy();
        lists();
        sets();
        queues();
        maps();
        iterationAndSorting();
        complexities();
    }

    // --- Иерархия --------------------------------------------------------

    static void hierarchy() {
        // Верх: Iterable<E> → Collection<E>.
        //   Collection → List, Set, Queue (и Deque).
        //   Map<K,V> — отдельная ветка (не Collection, потому что пары ключ-значение).
        //
        // Основные реализации:
        //   List:  ArrayList (массив),   LinkedList (двусвязный список)
        //   Set:   HashSet (хеш-таблица), LinkedHashSet (+порядок), TreeSet (красно-чёрное дерево)
        //   Queue: ArrayDeque, LinkedList, PriorityQueue (heap)
        //   Map:   HashMap, LinkedHashMap, TreeMap
        //
        // Все коллекции — обобщённые (Generics) и хранят только ссылочные типы.
        // Примитивы автоматически упаковываются в обёртки (int → Integer).
    }

    // --- List: упорядоченная коллекция с индексами -----------------------

    static void lists() {
        // ArrayList — массив с динамическим размером.
        //   get/set O(1), add в конец O(1) амортизированно,
        //   add/remove в середину O(n), contains/indexOf O(n).
        var list = new ArrayList<String>();
        list.add("b");
        list.add(0, "a");                  // вставка по индексу
        list.addAll(List.of("c", "d"));
        list.set(1, "B");
        System.out.println(list.get(0) + " " + list.size());

        // LinkedList — двусвязный список.
        //   add/remove по краям O(1), доступ по индексу O(n).
        //   Реализует и List, и Deque.
        var linked = new LinkedList<String>();
        linked.addFirst("head");
        linked.addLast("tail");
        linked.removeFirst();

        // List.of(...) — неизменяемый список фиксированного размера.
        // Обернуть в ArrayList, чтобы менять:
        var mutable = new ArrayList<>(List.of(1, 2, 3));
        mutable.add(4);

        // Полезные методы:
        //   size(), isEmpty(), clear(), contains(), indexOf(), lastIndexOf(),
        //   remove(int) — по индексу; remove(Object) — по значению (важно не путать!),
        //   subList(from, to), toArray().
        //   removeIf(Predicate) — фильтр на удаление.
        mutable.removeIf(x -> x % 2 == 0);
    }

    // --- Set: уникальные элементы ---------------------------------------

    static void sets() {
        // HashSet — на хеш-таблице. add/remove/contains O(1) в среднем.
        //   Порядок обхода не гарантирован. Требует корректных equals/hashCode.
        var hs = new HashSet<String>();
        hs.add("a"); hs.add("b"); hs.add("a");
        System.out.println(hs.size());     // 2

        // LinkedHashSet — хеш-таблица + двусвязный список: сохраняет порядок вставки.
        var lhs = new LinkedHashSet<String>();
        lhs.add("c"); lhs.add("a"); lhs.add("b");
        System.out.println(lhs);           // [c, a, b]

        // TreeSet — красно-чёрное дерево: элементы отсортированы.
        //   add/remove/contains O(log n).
        //   Есть навигация: first/last/lower/higher/headSet/tailSet/subSet/descendingSet.
        var ts = new TreeSet<String>();
        ts.add("c"); ts.add("a"); ts.add("b");
        System.out.println(ts);            // [a, b, c]
        System.out.println(ts.first() + ".." + ts.last());

        // Set.of(...) — неизменяемый набор.
        var fixed = Set.of("x", "y");
    }

    // --- Queue / Deque: очереди ------------------------------------------

    static void queues() {
        // Queue — FIFO: offer / poll / peek.
        // Deque — двусторонняя: addFirst/Last, pollFirst/Last, peekFirst/Last.

        // ArrayDeque — массив-кольцо: быстрая с обеих сторон.
        //   add/poll O(1) амортизированно.
        var deque = new ArrayDeque<String>();
        deque.add("a");                    // в хвост
        deque.addFirst("z");               // в голову
        deque.push("p");                   // = addFirst
        System.out.println(deque.poll());      // голова
        System.out.println(deque.pollLast());  // хвост

        // LinkedList тоже реализует Deque — годится и как очередь, и как список.

        // PriorityQueue — бинарная куча.
        //   add/offer/poll O(log n), peek O(1).
        //   Голова — минимальный элемент (или по Comparator).
        var pq = new PriorityQueue<Integer>();
        pq.offer(5); pq.offer(1); pq.offer(3);
        System.out.println(pq.poll());     // 1
        // С Comparator — обратный порядок, или сортировка по полю объекта.
        var maxHeap = new PriorityQueue<Integer>(Comparator.reverseOrder());
    }

    // --- Map: пары ключ-значение ----------------------------------------

    static void maps() {
        // HashMap — хеш-таблица. get/put/remove O(1) в среднем.
        //   Порядок не гарантирован.
        //   Один null-ключ допустим; equals/hashCode ключей должны быть согласованы.
        var map = new HashMap<Integer, String>();
        map.put(1, "a");
        map.put(2, "b");
        map.putIfAbsent(3, "c");
        map.replace(2, "B");
        System.out.println(map.get(1) + " " + map.containsKey(2) + " " + map.size());

        // Обход:
        for (var e : map.entrySet()) {
            System.out.println(e.getKey() + "=" + e.getValue());
        }
        for (var k : map.keySet()) { /* ... */ }
        for (var v : map.values()) { /* ... */ }
        // map.forEach((k, v) -> ...);

        // LinkedHashMap — сохраняет порядок вставки (или access-order при
        //   конструкторе с accessOrder=true — удобно для LRU-кэша).
        var lhm = new LinkedHashMap<String, Integer>();
        lhm.put("one", 1); lhm.put("two", 2); lhm.put("three", 3);

        // TreeMap — красно-чёрное дерево по ключам.
        //   get/put/remove O(log n). Навигация: firstKey, lastKey, lowerKey,
        //   higherKey, headMap, tailMap, subMap, descendingMap.
        var tm = new TreeMap<String, Integer>();
        tm.put("c", 3); tm.put("a", 1); tm.put("b", 2);
        System.out.println(tm.firstKey() + ".." + tm.lastKey());

        // Map.of / Map.ofEntries — неизменяемая карта.
        var fixed = Map.of(1, "a", 2, "b");
        var fixed2 = Map.ofEntries(Map.entry(1, "a"), Map.entry(2, "b"));
    }

    // --- Итерация и сортировка -------------------------------------------

    static void iterationAndSorting() {
        var list = new ArrayList<>(List.of("banana", "apple", "cherry"));

        // for-each работает через Iterable/Iterator.
        for (String s : list) System.out.print(s + " ");
        System.out.println();

        // Явный Iterator позволяет удалять во время обхода.
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().length() > 5) it.remove();
        }

        // Сортировка:
        //   list.sort(null)              — естественный порядок (Comparable);
        //   list.sort(comparator)        — внешний порядок;
        //   Collections.sort(list)       — устаревший вариант (то же).
        list.sort(Comparator.naturalOrder());
        list.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));

        // Разворот, перемешивание, частота:
        Collections.reverse(list);
        Collections.shuffle(list);
        System.out.println(Collections.frequency(list, "a"));

        // Неизменяемые обёртки:
        List<String> unmod = Collections.unmodifiableList(list);
        // — попытка add/remove бросит UnsupportedOperationException.
    }

    // --- Сложности --------------------------------------------------------

    static void complexities() {
        // Сводка (average / worst):
        //
        //                 get   add(конец)  add(i)  remove(i)  contains  iterate
        //   ArrayList      O(1)  O(1)*       O(n)    O(n)       O(n)      O(n)
        //   LinkedList     O(n)  O(1)        O(n)    O(n)       O(n)      O(n)
        //   HashSet        —     O(1)        —       O(1)       O(1)      O(n)
        //   LinkedHashSet  —     O(1)        —       O(1)       O(1)      O(n)
        //   TreeSet        —     O(log n)    —       O(log n)   O(log n)  O(n)
        //   ArrayDeque     —     O(1)*       O(n)    O(n)       O(n)      O(n)
        //   PriorityQueue  —     O(log n)    —       O(n)       O(n)      O(n)
        //   HashMap        O(1)  O(1)        —       O(1)       O(1)      O(n)
        //   TreeMap        O(log n) O(log n) —       O(log n)   O(log n)  O(n)
        //
        // * — амортизированно (при resize массива O(n), но раз в N вставок).
        //
        // Правила выбора:
        //   - частый доступ по индексу → ArrayList;
        //   - частые вставки/удаления по краям → ArrayDeque / LinkedList;
        //   - уникальность → Set; отсортированность → TreeSet / TreeMap;
        //   - пары ключ-значение → Map; порядок вставки → LinkedHashMap.
        //
        // Важно: у ключей HashMap и у элементов HashSet должны быть
        // корректно переопределены equals и hashCode.
    }
}
