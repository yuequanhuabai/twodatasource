package com.he.multi.multi.simple;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class CasBenchmark {

    private static final int OPS_PER_THREAD = 100_000;

    // 三种方式的共享变量
    private static AtomicInteger atomicVal = new AtomicInteger(0);
    private static LongAdder adderVal = new LongAdder();
    private static int syncVal = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) throws Exception {
        int[] threadCounts = {2, 10, 50, 100, 200, 500};

        System.out.printf("%-10s %15s %15s %15s%n", "线程数", "AtomicInteger", "LongAdder", "synchronized");
        System.out.println("---------------------------------------------------------------");

        for (int threadCount : threadCounts) {
            long t1 = testAtomicInteger(threadCount);
            long t2 = testLongAdder(threadCount);
            long t3 = testSynchronized(threadCount);
            System.out.printf("%-10d %12d ms %12d ms %12d ms%n", threadCount, t1, t2, t3);
        }
    }

    private static long testAtomicInteger(int threadCount) throws Exception {
        atomicVal.set(0);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Thread[] threads = new Thread[threadCount];

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPS_PER_THREAD; j++) {
                    atomicVal.incrementAndGet();
                }
                latch.countDown();
            });
            threads[i].start();
        }
        latch.await();
        return System.currentTimeMillis() - start;
    }

    private static long testLongAdder(int threadCount) throws Exception {
        adderVal.reset();
        CountDownLatch latch = new CountDownLatch(threadCount);
        Thread[] threads = new Thread[threadCount];

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPS_PER_THREAD; j++) {
                    adderVal.increment();
                }
                latch.countDown();
            });
            threads[i].start();
        }
        latch.await();
        return System.currentTimeMillis() - start;
    }

    private static long testSynchronized(int threadCount) throws Exception {
        syncVal = 0;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Thread[] threads = new Thread[threadCount];

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < OPS_PER_THREAD; j++) {
                    synchronized (lock) {
                        syncVal++;
                    }
                }
                latch.countDown();
            });
            threads[i].start();
        }
        latch.await();
        return System.currentTimeMillis() - start;
    }
}
