package com.he.multi.multi.simple.threadgroup.thread7;

public class Task7 implements Runnable {
    int times;
    Calcutor calcutor;

    public Task7(int times, Calcutor calcutor) {
        this.times = times;
        this.calcutor = calcutor;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            calcutor.increment();
        }
    }
}
