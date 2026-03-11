package com.he.multi.multi.simple.thread;

public class TaskThread implements Runnable{

    private int[] a;

    private final Object lock;

    public TaskThread(int[] a,Object lock) {
        this.a = a;
        this.lock = lock;
    }

    @Override
    public void run() {
         for (int i = 0; i < 1000; i++) {
             // 需要一把鎖把保證線程執行體的性質;
             synchronized (lock) {
                 a[0]++;
             }
         }
    }
}
