
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * Практика 5 — многопоточность.
 */
public class App {

    public static void main(String[] args) throws Exception {
        threadsAndRunnable();
        synchronization();
        executorsAndPools();
        synchronizersAndAtomics();
    }

    // --- 1. Потоки и Runnable ------------------------------------------------
    static void threadsAndRunnable() throws InterruptedException {
        // Runnable — задача, Thread — исполнитель.
        new Thread(() -> System.out.println("task on " + Thread.currentThread().getName())).start();

        // Свой поток с настройками.
        Thread worker = new Thread(() -> {
        });
        worker.setName("worker");
        worker.setDaemon(true);
        worker.start();
        worker.join();

        // Прерывание спящего потока.
        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("not reached");
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        });
        sleeper.start();
        sleeper.interrupt();
        sleeper.join();

        // Порядок между потоками недетерминирован.
        Runnable printer = () -> System.out.print(Thread.currentThread().getName() + " ");
        for (int i = 0; i < 5; i++) {
            new Thread(printer).start();
        }
        Thread.sleep(100);
        System.out.println();
    }

    // --- 2. Гонки, synchronized, volatile, wait/notify ----------------------
    static void synchronization() throws InterruptedException {
        // counter++ — не атомарная операция: load → add → store.
        class Shared {

            int counter = 0;

            synchronized void up() {
                counter++;
            }

            synchronized void down() {
                counter--;
            }
        }
        Shared sh = new Shared();
        Thread a = new Thread(sh::up);
        Thread b = new Thread(sh::down);
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("counter = " + sh.counter);

        // volatile — видимость без кэша, но не атомарность.
        class Loop extends Thread {

            volatile boolean done = false;

            public void run() {
                while (!done) {
                }
            }
        }
        Loop loop = new Loop();
        loop.start();
        Thread.sleep(50);
        loop.done = true;
        loop.join();
        System.out.println("loop stopped");

        // wait / notify — обмен через монитор объекта.
        class Box {

            private int value;
            private boolean ready;

            synchronized void put(int v) throws InterruptedException {
                while (ready) {
                    wait();
                }
                value = v;
                ready = true;
                notifyAll();
            }

            synchronized int get() throws InterruptedException {
                while (!ready) {
                    wait();
                }
                ready = false;
                notifyAll();
                return value;
            }
        }
        Box box = new Box();
        Thread producer = new Thread(() -> {
            try {
                box.put(100);
            } catch (InterruptedException ignored) {
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("got " + box.get());
            } catch (InterruptedException ignored) {
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    // --- 3. Executor, пулы, ForkJoin, CompletableFuture ----------------------
    static void executorsAndPools() throws Exception {
        // Executor — абстракция исполнителя.
        Executor executor = task -> new Thread(task).start();
        executor.execute(() -> System.out.println("via Executor"));

        // ExecutorService + Callable + Future.
        ExecutorService service = Executors.newFixedThreadPool(2);
        Callable<Boolean> task = () -> "very long text".contains("long");
        Future<Boolean> future = service.submit(task);
        System.out.println("future.get = " + future.get());
        service.shutdown();

        // Пул потоков + батч задач.
        String[] words = {"CO2", "H2O", "NaCl"};
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (String w : words) {
            Future<Boolean> f = pool.submit(() -> w.contains("O"));
            System.out.println(w + " -> " + f.get());
        }
        pool.shutdown();

        // ForkJoin: divide and conquer, work stealing.
        class DoubleTask extends RecursiveAction {

            static final int THRESHOLD = 1_000_000;
            final int[] array;
            final int lo, hi;

            DoubleTask(int[] a, int lo, int hi) {
                array = a;
                this.lo = lo;
                this.hi = hi;
            }

            protected void compute() {
                if (hi - lo < THRESHOLD) {
                    for (int i = lo; i < hi; i++) {
                        array[i] *= 2;
                    }
                } else {
                    int mid = (lo + hi) / 2;
                    invokeAll(new DoubleTask(array, lo, mid),
                            new DoubleTask(array, mid, hi));
                }
            }
        }
        int[] array = new int[4_000_000];
        Arrays.fill(array, 1);
        ForkJoinPool.commonPool().invoke(new DoubleTask(array, 0, array.length));
        System.out.println("array[0] = " + array[0]);

        // CompletableFuture — композиция асинхронных шагов.
        CompletableFuture
                .supplyAsync(() -> "result")
                .thenApply(String::toUpperCase)
                .thenAccept(System.out::println)
                .join();
    }

    // --- 4. Синхронизаторы и атомарные коллекции ----------------------------
    static void synchronizersAndAtomics() throws Exception {
        // Lock / Condition — гибче synchronized.
        Lock lock = new ReentrantLock();
        Condition notEmpty = lock.newCondition();
        lock.lock();
        try {
            // критическая секция
        } finally {
            lock.unlock();
        }

        // Semaphore — ограничение числа одновременных доступов.
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        try {
            // ... критическая работа ...
        } finally {
            semaphore.release();
        }

        // CountDownLatch — ждать N событий.
        CountDownLatch latch = new CountDownLatch(2);
        Runnable worker = () -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
            latch.countDown();
        };
        new Thread(worker).start();
        new Thread(worker).start();
        latch.await();
        System.out.println("latch opened");

        // BlockingQueue — producer / consumer без ручной синхронизации.
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        Thread producer = new Thread(() -> {
            try {
                queue.put(42);
            } catch (InterruptedException ignored) {
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("took " + queue.take());
            } catch (InterruptedException ignored) {
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // AtomicInteger — CAS-инкремент без блокировок.
        AtomicInteger counter = new AtomicInteger();
        counter.incrementAndGet();
        counter.addAndGet(10);
        System.out.println("atomic = " + counter.get());

        // ConcurrentHashMap + LongAdder — частотный анализ.
        var freq = new ConcurrentHashMap<String, LongAdder>();
        for (String key : List.of("a", "b", "a", "c", "a")) {
            freq.computeIfAbsent(key, k -> new LongAdder()).increment();
        }
        System.out.println("freq = " + freq);

        // CopyOnWriteArrayList — read-heavy сценарии.
        var list = new CopyOnWriteArrayList<String>();
        list.add("x");
    }
}
