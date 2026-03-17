package com.he.multi.multi.simple.threadgroup.add.thread1;

/**
 * 线程执行体，核心是规定线程执行逻辑;
 * 需要线程操作的共享变量和线程控制的锁对象 即：共享资源和锁控制;
 */
public class Task1 implements Runnable {
    private Object o;
    private int a[];
    private final int count = 1000;

    public Task1(Object o, int[] a) {
        this.o = o;
        this.a = a;
    }


    @Override
    public void run() {

        for (int j = 0; j < count; j++) {
            synchronized (o) {
                a[0]++;
            }
        }
    }
}
