package com.he.multi.multi.simple.threadgroup.cas.varhandle;

public class MyCASCounterTest2 {
    public static void main(String[] args) throws InterruptedException {
        MyCASCounter2 counter = new MyCASCounter2();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("result: " + counter.get());
    }
}
