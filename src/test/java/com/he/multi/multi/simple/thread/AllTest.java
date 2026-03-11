package com.he.multi.multi.simple.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  當前類負責主體框架
 */
public class AllTest {




    // 執行體
   private static final Object aa= new Object();

    public static void main(String[] args) throws InterruptedException {
        // 線程本身
        Thread[] threads =new Thread[10];
// 共享變量，即所有線程的操作變量;
        int[] a = {0};
        // 線程鎖對象來保證線程的操作三性;

            for (int i = 0; i < 10; i++) {
               threads[i]= new Thread(new TaskThread(a,aa));
               threads[i].start();
            }

            for (int i = 0; i < 10; i++) {
                threads[i].join();
            }

        System.out.println("a: "+a[0]);

    }
}
