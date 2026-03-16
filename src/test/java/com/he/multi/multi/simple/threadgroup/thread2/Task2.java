package com.he.multi.multi.simple.threadgroup.thread2;

import java.util.concurrent.atomic.AtomicInteger;

public class Task2 extends Thread {
    private AtomicInteger a;

    public Task2(AtomicInteger a) {
        this.a = a;
    }


    @Override
    public void run() {
        for (int j = 0; j < 1000; j++) {
            a.addAndGet(1);
        }

    }
}
