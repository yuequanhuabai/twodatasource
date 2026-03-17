package com.he.multi.multi.simple.threadgroup.cas.unsafe;

// 需要使用jdk8環境去運行代碼
public class MyCASCounterTest {

    public static void main(String[] args) throws InterruptedException {
        MyCASCounter counter = new MyCASCounter();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet();
                }

            });
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("result: " + counter.get());
    }
}
