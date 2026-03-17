package com.he.multi.multi.simple.threadgroup.add.thread4;

public class Test4 {

    static int[] a = {0};
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Task4(a, lock);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("a[0]=" + a[0]);

    }
}
