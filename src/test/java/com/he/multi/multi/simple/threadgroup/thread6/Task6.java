package com.he.multi.multi.simple.threadgroup.thread6;

public class Task6 implements Runnable {

    private Counter counter;
    private int times;

    public Task6(Counter counter, int times) {
        this.counter = counter;
        this.times = times;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            counter.increment();
        }
    }
}
