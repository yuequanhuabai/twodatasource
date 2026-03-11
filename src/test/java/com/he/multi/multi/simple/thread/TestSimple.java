package com.he.multi.multi.simple.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestSimple {

    // 题目: 启动10个线程，每个线程执行一千次自增操作,最终的结果是10000;
    public static void main(String[] args) throws InterruptedException {

        int[] counter = {0};
        Lock lock = new ReentrantLock();
        Thread[] threads = new Thread[10];
        Integer a = 0;

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new IncrementTask(counter, lock));
            threads[i].start();
        }
        // 主线程一次加入到thread[i]中去; 直道最后一个thread[9]执行完毕;
        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println("a:" + counter[0]);
    }
}
