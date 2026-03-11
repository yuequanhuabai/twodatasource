package com.he.multi.multi.simple.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IncrementTask implements Runnable {

    private final int[] counter;
    private final Lock lock;

    public IncrementTask(int[] counter, Lock lock) {
        this.counter = counter;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int j = 0; j < 1000; j++) {
            lock.lock();
            try {
                counter[0]++;
            } finally {
                lock.unlock();
            }
        }
    }
}
