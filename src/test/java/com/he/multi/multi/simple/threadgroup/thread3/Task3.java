package com.he.multi.multi.simple.threadgroup.thread3;

public class Task3 extends Thread {

    private int[] a;

    public Task3(int[] a) {
        this.a = a;
    }

    public synchronized int add() {

        return a[0]++;
    }

    public synchronized int get() {
        return a[0];
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            add();
        }
    }
}
