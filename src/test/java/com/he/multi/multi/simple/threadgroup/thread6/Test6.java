package com.he.multi.multi.simple.threadgroup.thread6;

public class Test6 {

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];

        Counter counter = new Counter();

        final int times = 1000;


        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Task6(counter, times));
        }

        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("count:" + counter.getCount());
    }

}
