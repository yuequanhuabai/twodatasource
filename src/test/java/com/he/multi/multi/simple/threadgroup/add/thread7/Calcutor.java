package com.he.multi.multi.simple.threadgroup.add.thread7;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Calcutor {

    private final Lock lock = new ReentrantLock();


    private int a = 0;


    public int increment() {

        lock.lock();
        try {
            return a++;
        } finally {
            lock.unlock();
        }
    }


    public int getA() {
        lock.lock();
        try {
            return a;
        } finally {
            lock.unlock();
        }
    }

}
