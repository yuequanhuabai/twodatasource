package com.he.multi.multi.simple.threadgroup.cas.compare;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * **LongAdder vs AtomicLong 性能对比**
 * <p>
 * 编写一个性能测试程序，对比 `AtomicLong` 和 `LongAdder` 在高并发场景下的性能差异。
 * <p>
 * 要求：
 * 1. 分别使用 AtomicLong 和 LongAdder
 * 2. 启动100个线程，每个线程执行100万次累加
 * 3. 记录两种方式的耗时
 * 4. 分析性能差异的原因
 */
public class Test1 {


    public static void main(String[] args) throws InterruptedException {
//        // AtomicLong
//        long start1 = System.nanoTime();
//        testAtomicLong();
//        long end1 = System.nanoTime();
//        System.out.println("AtomicLong cost: " + (end1 - start1) / 1_000_000 + " ms");
//
//        System.out.println("---------------------------");

        // LongAdder
        long start2 = System.nanoTime();
        testLongAdder();
        long end2 = System.nanoTime();
        System.out.println("LongAdder  cost: " + (end2 - start2) / 1_000_000 + " ms");
    }

    public static void testAtomicLong() throws InterruptedException {
        Thread[] threads = new Thread[100];
        AtomicLong atomicLong = new AtomicLong(0);

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(
                    () -> {
                        for (int j = 0; j < 1000000; j++) {
                            atomicLong.getAndAdd(1);
                        }
                    }
            );
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();

        }

        System.out.println("AtomicLong result: " + atomicLong.get());
    }


    public static void testLongAdder() throws InterruptedException {
        Thread[] threads = new Thread[100];
        LongAdder longAdder = new LongAdder();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() ->
            {
                for (int j = 0; j < 1000000; j++) {
                    longAdder.increment();
                }
            }
            );
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("sum:" + longAdder.sum());
    }

//    public static void testLongAdder() throws InterruptedException {
//        Thread[] threads = new Thread[100];
//        LongAdder longAdder = new LongAdder();
//
//        for (int i = 0; i < threads.length; i++) {
//            threads[i] = new Thread(
//                    () -> {
//                        for (int j = 0; j < 1000000; j++) {
//                            longAdder.increment();
//                        }
//                    }
//            );
//            threads[i].start();
//        }
//
//        for (int i = 0; i < threads.length; i++) {
//            threads[i].join();
//        }
//
//        System.out.println("LongAdder  result: " + longAdder.sum());
//    }
}
