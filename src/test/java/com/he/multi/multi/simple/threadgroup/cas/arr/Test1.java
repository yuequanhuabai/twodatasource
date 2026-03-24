package com.he.multi.multi.simple.threadgroup.cas.arr;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        AtomicIntegerArray a = new AtomicIntegerArray(10);
        for (int i = 0; i < 10; i++) {
            threads[i]=  new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    int i1 = ThreadLocalRandom.current().nextInt(1, 101);
                    int i2 = (i1 - 1) / 10;
                    a.getAndAdd(i2, 1);
                }
            });
        }

        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("a.get("+i+"): "+a.get(i));
        }
    }
}
