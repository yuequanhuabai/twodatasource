package com.he.multi.multi.simple.threadgroup.add.thread6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;

    private final Lock lock = new ReentrantLock();


    public int increment() {
        lock.lock();
        try {
            return count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }


}
