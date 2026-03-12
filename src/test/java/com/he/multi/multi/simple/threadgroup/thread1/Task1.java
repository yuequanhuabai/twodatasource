package com.he.multi.multi.simple.threadgroup.thread1;

/**
 * 线程执行体，核心是规定线程执行逻辑;
 * 需要线程操作的共享变量和线程控制的锁对象 即：共享资源和锁控制;
 */
public class Task1 implements Runnable {
    private Object o;
    private int i;

    public Task1(Object o, int i) {
        this.o = o;
        this.i = i;
    }


    @Override
    public void run() {
        synchronized (o) {
            i++;
        }

    }
}
