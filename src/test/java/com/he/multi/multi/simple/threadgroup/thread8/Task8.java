package com.he.multi.multi.simple.threadgroup.thread8;

public class Task8 implements Runnable {
    private Calcutor8 calcutor8;

    private int times;

    public Task8(Calcutor8 calcutor8, int times) {
        this.calcutor8 = calcutor8;
        this.times = times;
    }

    @Override
    public void run() {

        for(int i=0;i<times;i++){
            calcutor8.increment();
        }

    }
}
