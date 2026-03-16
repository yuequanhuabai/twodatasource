package com.he.multi.multi.simple.threadgroup.thread2;

import java.util.concurrent.atomic.AtomicInteger;

public class Test2 {

    // 执行者
    private static final Thread[] threads = new Thread[10];

    //     共享变量
    static AtomicInteger atomicInteger = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < threads.length; i++) {
//            threads[i] = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int j = 0; j < 1000; j++) {
//                        atomicInteger.addAndGet(1);
//                    }
//                }
//            });
            threads[i] = new Task2(atomicInteger);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("atomicInteger: " + atomicInteger);
    }
}
