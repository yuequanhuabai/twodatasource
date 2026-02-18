package com.he.multi.multi.simple;

import static sun.misc.PostVMInitHook.run;

public class TestSimple {
    public static  int a = 0;

    // 题目: 启动10个线程，每个线程执行一千次自增操作,最终的结果是10000;
    public static void main(String[] args) throws InterruptedException {

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(
                    () -> {
                        for (int j = 0; j < 1000; j++) {
                            synchronized (Object.class) {
                                a++;
                            }
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
