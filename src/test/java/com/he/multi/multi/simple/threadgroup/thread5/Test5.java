package com.he.multi.multi.simple.threadgroup.thread5;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test5 {

    static Lock lock = new ReentrantLock();
    static int[] a = {0};
    static int count = 1000;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Task5(a, count, lock);

        }

        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("a[0]=" + a[0]);

    }
}
