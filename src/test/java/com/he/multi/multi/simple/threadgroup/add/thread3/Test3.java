package com.he.multi.multi.simple.threadgroup.add.thread3;

/**
 *  错误示例：锁非共享;
 */
public class Test3 {

    static Thread[] threads = new Thread[10];
    private static int[] a = {0};

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Task3(a);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("a[0]: " + a[0]);

    }
}
