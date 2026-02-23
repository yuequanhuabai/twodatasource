package com.he.multi.multi.simple;

public class TestSimple {
    public static  volatile int a = 0;

    // 题目: 启动10个线程，每个线程执行一千次自增操作,最终的结果是10000;
    public static void main(String[] args) throws InterruptedException {

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(
                    () -> {
                        for (int j = 0; j < 1000; j++) {
                                a++;
                        }
                    }
            );
            threads[i].start();
        }
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("a:" + a);
    }
}
