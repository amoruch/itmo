import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * Урок 2.5 — многопоточность.
 */
public class App {

    public static void main(String[] args) throws Exception {
        threadsAndRunnable();
        synchronization();
        executorsAndPools();
        synchronizersAndAtomics();
    }

    // --- 1. Потоки, Runnable и virtual threads ------------------------------
    static void threadsAndRunnable() throws InterruptedException {
        // Runnable — задача, Thread — исполнитель.
        Thread task = new Thread(() -> System.out.println("task on " + Thread.currentThread().getName()));
        task.start();
        task.join();

        Thread worker = new Thread(() -> {
        }, "worker");
        worker.setDaemon(true);
        worker.start();
        worker.join();

        // Virtual thread — лёгкий поток JVM, удобный для множества I/O-задач.
        Thread virtual = Thread.ofVirtual()
                .name("virtual-worker")
                .start(() -> System.out.println("virtual: " + Thread.currentThread().isVirtual()));
        virtual.join();

        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(1_000);
                System.out.println("not reached");
            } catch (InterruptedException e) {
                System.out.println("interrupted");
                Thread.currentThread().interrupt();
            }
        });
        sleeper.start();
        sleeper.interrupt();
        sleeper.join();

        // Порядок между потоками недетерминирован, но join() ждёт каждый из них.
        Runnable printer = () -> System.out.print(Thread.currentThread().getName() + " ");
        List<Thread> printers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread printerThread = new Thread(printer);
            printers.add(printerThread);
            printerThread.start();
        }
        for (Thread printerThread : printers) {
            printerThread.join();
        }
        System.out.println();
    }

    // --- 2. Гонки, synchronized, volatile, wait/notify ----------------------
    static void synchronization() throws InterruptedException {
        // counter++ — не атомарная операция: load → add → store.
        class Shared {

            private int counter;

            synchronized void up() {
                counter++;
            }
        }
        Shared shared = new Shared();
        List<Thread> incrementers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Thread incrementer = new Thread(() -> {
                for (int j = 0; j < 10_000; j++) {
                    shared.up();
                }
            });
            incrementers.add(incrementer);
            incrementer.start();
        }
        for (Thread incrementer : incrementers) {
            incrementer.join();
        }
        System.out.println("counter = " + shared.counter);

        // volatile — видимость между потоками, но не атомарность составных операций.
        class Loop extends Thread {

            volatile boolean done;

            public void run() {
                while (!done) {
                    Thread.onSpinWait();
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

            synchronized void put(int nextValue) throws InterruptedException {
                while (ready) {
                    wait();
                }
                value = nextValue;
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
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("got " + box.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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
        ExecutorService service = Executors.newSingleThreadExecutor();
        try {
            Executor executor = service;
            executor.execute(() -> System.out.println("via Executor"));
            Callable<Boolean> task = () -> "very long text".contains("long");
            System.out.println("future.get = " + service.submit(task).get());
        } finally {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.SECONDS);
        }

        // Сначала отправляем все задачи, затем ждём результаты.
        String[] words = {"CO2", "H2O", "NaCl"};
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<Boolean>> futures = new ArrayList<>();
            for (String word : words) {
                futures.add(pool.submit(() -> word.contains("O")));
            }
            for (int i = 0; i < words.length; i++) {
                System.out.println(words[i] + " -> " + futures.get(i).get());
            }
        } finally {
            pool.shutdown();
            pool.awaitTermination(1, TimeUnit.SECONDS);
        }

        // ForkJoin: divide and conquer, work stealing.
        class DoubleTask extends RecursiveAction {

            static final int THRESHOLD = 10_000;
            final int[] array;
            final int lo;
            final int hi;

            DoubleTask(int[] array, int lo, int hi) {
                this.array = array;
                this.lo = lo;
                this.hi = hi;
            }

            protected void compute() {
                if (hi - lo <= THRESHOLD) {
                    for (int i = lo; i < hi; i++) {
                        array[i] *= 2;
                    }
                    return;
                }

                int mid = (lo + hi) / 2;
                invokeAll(new DoubleTask(array, lo, mid), new DoubleTask(array, mid, hi));
            }
        }
        int[] array = new int[100_000];
        Arrays.fill(array, 1);
        ForkJoinPool.commonPool().invoke(new DoubleTask(array, 0, array.length));
        System.out.println("array[0] = " + array[0]);

        CompletableFuture
                .supplyAsync(() -> "result")
                .thenApply(String::toUpperCase)
                .thenAccept(System.out::println)
                .join();
    }

    // --- 4. Синхронизаторы и атомарные коллекции ----------------------------
    static void synchronizersAndAtomics() throws Exception {
        // Lock / Condition — гибче synchronized и позволяют несколько очередей ожидания.
        Lock lock = new ReentrantLock();
        Condition changed = lock.newCondition();
        boolean[] ready = {false};
        Thread signaler = new Thread(() -> {
            lock.lock();
            try {
                ready[0] = true;
                changed.signalAll();
            } finally {
                lock.unlock();
            }
        });
        lock.lock();
        try {
            signaler.start();
            while (!ready[0]) {
                changed.await();
            }
        } finally {
            lock.unlock();
        }
        signaler.join();
        System.out.println("condition signalled");

        // Semaphore — ограничение числа одновременных доступов.
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        try {
            System.out.println("semaphore acquired");
        } finally {
            semaphore.release();
        }

        CountDownLatch latch = new CountDownLatch(2);
        Runnable latchWorker = () -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        };
        new Thread(latchWorker).start();
        new Thread(latchWorker).start();
        latch.await();
        System.out.println("latch opened");

        // BlockingQueue — producer / consumer без ручной синхронизации.
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        Thread producer = new Thread(() -> {
            try {
                queue.put(42);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                System.out.println("took " + queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        AtomicInteger counter = new AtomicInteger();
        counter.incrementAndGet();
        counter.addAndGet(10);
        System.out.println("atomic = " + counter.get());

        var freq = new ConcurrentHashMap<String, LongAdder>();
        for (String key : List.of("a", "b", "a", "c", "a")) {
            freq.computeIfAbsent(key, ignored -> new LongAdder()).increment();
        }
        System.out.println("freq = " + freq);

        // CopyOnWriteArrayList подходит для редких записей и частых чтений.
        var listeners = new CopyOnWriteArrayList<String>();
        listeners.add("listener");
        listeners.forEach(System.out::println);
    }
}