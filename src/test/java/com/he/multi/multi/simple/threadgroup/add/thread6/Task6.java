package com.he.multi.multi.simple.threadgroup.add.thread6;

public class Task6 implements Runnable {

    private final Counter counter;
    private final int times;

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
