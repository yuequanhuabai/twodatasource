package com.he.multi.multi.simple.threadgroup.thread5;

import java.util.concurrent.locks.Lock;

public class Task5 extends Thread {

    // 共享變臉；
    private final int[] a;

    // 鎖
    private final Lock lock;

    // 任務體的執行次數
    private final int count;

    public Task5(int[] a, int count, Lock lock) {
        this.a = a;
        this.count = count;
        this.lock = lock;
    }


    @Override
    public void run() {

        for (int i = 0; i < count; i++) {
            lock.lock();
            try {
                a[0]++;
            } finally {
                lock.unlock();
            }

        }

    }
}
