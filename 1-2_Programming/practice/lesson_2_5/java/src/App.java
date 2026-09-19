
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

public class App {

    public static void main(String[] args) throws Exception {
        part1();
    }

    static void part1() throws InterruptedException {
        System.out.println("part1: multithreading");

        // runnable
        class TaskR implements Runnable {

            @Override
            public void run() {
                /* thread body (logic) */
            }
        }
        new Thread(new TaskR()).start();

        // thread
        class TaskT extends Thread {

            @Override
            public void run() {
                /* logic */
            }
        }
        new TaskT().start();

        // lambda
        new Thread(() -> {/* logic */
        }).start();

        // thread states
        // new -> (ready -> runnable?(run())) -> terminated
        System.out.println(Thread.currentThread().getName());
        Thread tmp = new Thread(() -> {
        });
        // tmp.getID(); // deprecated? myoldversion?
        tmp.setName("aboba");
        tmp.getPriority();
        tmp.setPriority(1);
        tmp.getState();
        tmp.isAlive();
        tmp.isDaemon();
        tmp.setDaemon(true);

        Thread.sleep(10);

        tmp.join();

        // yield(); // ?
        // sleeping between 'ready' and 'run()'
        tmp.interrupt();

        tmp.start();

        Runnable r = () -> {
            String name = Thread.currentThread().getName();
            System.out.println(name + "started");
            try {
                Thread.sleep(500 + (long) (100 * Math.random()));
            } catch (InterruptedException e) {
                return;
            }
            System.out.println(name + " finished");
        };

        for (int i = 0; i < 10; i++) {
            (new Thread(r)).start();
        }

        class Shared {

            int counter = 0;

            void up() {
                counter++;
            }

            void down() {
                counter--;
            }
        }

        Shared sh = new Shared();
        new Thread(sh::up).start();
        new Thread(sh::down).start();
        System.out.println(sh.counter);

        // race condition
        // solution(s, because there are many)
        class SharedS {

            int counter = 0;

            synchronized void up() {
                counter++;
            }

            synchronized void down() {
                counter--;
            }
        }

        SharedS shs = new SharedS();
        new Thread(shs::up).start();
        new Thread(shs::down).start();
        System.out.println(shs.counter);

        // syncronized works via monitor (shit for obj to 'monitor' who uses methods?)
        // blocked between 'ready' and 'run()'
        // synchronized method - only one thread can access
        // sync class - only one thread can work with at a time
        // sync static method - other static are blocked too?
        // cache problem - diff threads see diff values
        // JVM optimization mechanic
        class Opt extends Thread {

            boolean done = false;
            long i = 0;

            @Override
            public void run() {
                while (!done) {
                    i++; // if (!done) while (true) i++;

                }
            }
        }

        var opt = new Opt();
        opt.start();
        Thread.sleep(1000);
        opt.done = true;

        // JVM can change order of ops (optimization )
        // volatile - shows, that this var can be changed from another thread
        // so JVM would be careful optimizing this shit
        // and volatile dont use cache
        // so its longer, but reliable
        // happens-before mechanic (JMM - Java Memory Model)
        // 1. common variable and flag
        class Block {

            volatile boolean ready;
            int value;

            void put(int i) {
                while (ready);
                synchronized (this) {
                    value = i;
                    ready = true;
                }
            }

            int get() {
                while (!ready);
                synchronized (this) {
                    ready = false;
                    return value;
                }
            }
        }

        Block g = new Block();

        Thread t1 = new Thread(() -> {
            g.put(100);
        });
        Thread t2 = new Thread(() -> {
            g.get();
        });

        t1.start();
        t2.start();

        // 2. wait / notify
        class Block2 {

            volatile boolean ready;
            int value;

            synchronized void put(int i) {
                while (ready) try {
                    wait();
                } catch (InterruptedException e) {
                };
                value = i;
                ready = true;
                notifyAll();
            }

            synchronized int get() {
                while (!ready) try {
                    wait();
                } catch (InterruptedException e) {
                };
                ready = false;
                notifyAll();
                return value;
            }
        }

        Block2 g2 = new Block2();

        Thread t12 = new Thread(() -> {
            g2.put(100);
        });
        Thread t22 = new Thread(() -> {
            g2.get();
        });

        t12.start();
        t22.start();

        // timed_waiting/sleeping between 'ready' and 'run()' 
    }

    static void part2() throws InterruptedException, ExecutionException {
        System.out.println("part2: java.concurrent");

        // deadlock problem
        // livelock
        // starvation
        // non determinent

        /*
        java.util.concurrent
        interfaces: Executor, Callable, Future;
        classes: ThreadPoolExecutor, ForkJoinPool;
        sync-classes;
        interfaces: BlockingQueue, TransferQueue;
        collections: Concurrent, CopyOnWrite;
        
        java.util.concurrent.locks
        interfaces: Lock, Condition

        java.util.concurrent.atomic
        AtomicInteger, AtomicLong, AtomicReference
         */
        // Executor - abstract executioner
        // void execute (Runnable task)
        class myExecutor implements Executor {

            public myExecutor() {
            }

            @Override
            public void execute(Runnable task) {
                (new Thread(task)).start();
            }
        }

        Runnable task1 = () -> {
        };
        Executor executor = new myExecutor();
        executor.execute(task1);

        /*
        interface ExecutorService extends Executor
            Future<T> submit(Callable<T> task)
            void shutdown()
            List<Runnable> shutdownNow()
            List<Future<T>> invokeAll(Collection<Callable<T>> tasks)

        interface Callable<T>
            T call()
        
        interface Future<T>
            T get()
            boolean isDone()
            boolean cancel()
         */
        var s = "toFind";
        var text = "very long text";

        ExecutorService service = Executors.newFixedThreadPool(4);
        Callable<Boolean> task = () -> search(s, text);
        Future<Boolean> future = service.submit(task);

        while (!future.isDone()) {
            Thread.sleep(100);
        }

        boolean res = future.get();
        System.out.println(res);

        // scheduledExecutorService
        // pools - reuse of threads
        // ThreadPoolExecutor - implements ExecutorService
        // corePoolSize, maximumPoolSize, keepAliveTime
        // Executors - static methods for creating ExecutorServices
        String[] arr = {"CO2", "H2O", "NaCl"};

        var pool = Executors.newFixedThreadPool(2);
        for (String elem : arr) {
            var future2 = pool.submit(() -> search(elem, text));
            System.out.println(future2.get());
        }

        // ForkJoin framework (parralel, divide and conquer, work stealing)
        // ForkJoinPool, ForkJoinTaskm RecursiveAction, RecursiveTask
        class Task extends RecursiveAction {

            final int[] array;
            final int lo, hi;
            final static int SIZE = 10;

            Task(int[] array, int lo, int hi) {
                this.array = array;
                this.lo = lo;
                this.hi = hi;
            }

            @Override
            protected void compute() {
                if ((hi - lo) < SIZE) {
                    for (int i = lo; i < hi; i++) {
                        array[i] *= 2;
                    }
                } else {
                    int mid = (lo + hi) / 2;
                    var task1 = new Task(array, lo, mid);
                    var task2 = new Task(array, mid, hi);
                    task1.fork();
                    task2.fork();
                    task2.join();
                    task1.join();
                }
            }
        }

        int[] array = new int[33554432];
        Arrays.parallelSetAll(array, i -> 1);
        var bigtask = new Task(array, 0, array.length);
        var FJPool = ForkJoinPool.commonPool();
        FJPool.invoke(bigtask);

        /* CompletableFuture

        CompletableFuture
            .supplyAsync( () -> getResult() )
            .thenApply( String::toUpperCase )
            .thenAccept( System.out::println );
         */
        // Parallel Streams
        // Spliterators (Stream API)
        // interface Lock - synchonized analog
        // interface Condition - wait-notify analog
        // ReentrantLock, ReadriteLock, ReentrantReadriteLock
        Lock lock = new ReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();
        int[] values = new int[100];
        int count = 0;

        // semaphore
        // countDownLatch
        // CyclicBarrier
        // Phaser
        // Exchanger<V>
        // BlockingQueue, BlockingDeque
        // ArrayBlockingQueue
        // LinkedBlockingQueue
        // LinkedBlockingDeque
        // PriorityBlockingQueue
        // DelayQueue<E extends Delayed>
        // SynchronousQueue
        // interface TransferrQueue extends BlockingQueue
        // LinkedTransferQueue implements TransferQueue
        // ConcurrentMap, ConcurrentNavigableMap
        // ConcurrentLinkedQueue
        // CopyOnWriteArrayList / CopyOnWriteArraySet
        // atomic operations (CAS, compare and swap)
        class fakeCAS {

            int value;
            atomic

            int cmpxchg(int expected, int updated) {
                int old = value;
                if (old == expected) {
                    value = updated;
                }
                return old;
            }
            atomic

            int get() {
                return value;
            }
        }

        var fcas = new fakeCAS();
        /*
        int increment() {
            int v;
            do {
                v = fcas.get();
            } while (v != fcas.cmpxchg(v, v + 1));
            return v + 1;
        }
         */

        // java.util.concurrent.atomic
        // AtomicInteger, AtomicLong
        // AtomicBoolean, AtomicReference
        // AtomicIntegerArray
        // LongAccumulator, DoubleAccumulator
        // LongAdder, DoubleAdder
        class Count {

            AtomicInteger counter = new AtomicInteger(0);

            public void up() {
                counter.incrementAndGet();
            }

            public void down() {
                counter.decrementAndGet();
            }
        }

        List<String> keys; // список строк для подсчета
        ConcurrentHashMap<String, LongAdder> counter; // счетчик
        counter = new ConcurrentHashMap<>();
        for (String key : keys) {
            counter.computeIfAbsent(key, LongAdder::new).increment();
        }
    }

    static public boolean search(String s, String text) {
        return text.contains(s);
    }
}
