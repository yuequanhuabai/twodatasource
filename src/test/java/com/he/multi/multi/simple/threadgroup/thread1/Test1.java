package com.he.multi.multi.simple.threadgroup.thread1;

/**
 * 题目: 启动10个线程，每个线程执行一千次自增操作,最终的结果是10000;
 * <p>
 * 主体控制类，掌控控制流程
 */
public class Test1 {

    // 需要的原材料：10个线程
    private static final Thread[] threads = new Thread[10];
    //    操作的共享变量
    static int a = 0;
    //  共享资源控制锁
    private static final Object lock = new Object();


    public static void main(String[] args) {

        for(int i=0;i<threads.length;i++){
            threads[i]=new Thread(new Task1(lock,a));
        }

        for(int i=0;i<threads.length;i++){

        }

    }
}
