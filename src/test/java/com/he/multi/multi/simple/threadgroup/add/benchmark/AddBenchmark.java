package com.he.multi.multi.simple.threadgroup.add.benchmark;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class AddBenchmark {

    public static void main(String[] args) throws InterruptedException {
        // 测试AddLonger和AtomicLong的性能
        testAtomicLong();

        testLongAdder();
    }

    public static void testLongAdder() throws InterruptedException {
        LongAdder longAdder = new LongAdder();
        Thread[] threads = new Thread[100];
        long start = System.nanoTime();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000_000; j++) {
                    longAdder.increment();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        long end = System.nanoTime();
        System.out.println("LongAdder cost: " + (end - start) / (1000 * 1000) + "ms  " + "result: " + longAdder.sum());
    }

    public static void testAtomicLong() throws InterruptedException {
        AtomicLong atomicLong = new AtomicLong(0);

        Thread[] threads = new Thread[100];
        long start = System.nanoTime();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000_000; j++) {
                    atomicLong.getAndAdd(1);
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        long end = System.nanoTime();
        System.out.println("AtomicLong cost: " + (end - start) / (1000 * 1000) + "ms  " + "result: " + atomicLong.get());
    }


}
