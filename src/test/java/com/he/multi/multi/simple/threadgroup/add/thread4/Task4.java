package com.he.multi.multi.simple.threadgroup.add.thread4;

public class Task4 extends Thread {

    private int[] b;
    private  Object c;

    public int[] increment() {
        synchronized (c) {
            b[0]++;
        }
        return b;
    }

    public int[] getB() {
        synchronized (c) {
            return b;
        }

    }

    public Task4(int[] b, Object c) {
        this.b = b;
        this.c = c;
    }


    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            b[0]++;
        }
    }
}
