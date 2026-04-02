package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 Semaphore 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：Semaphore 是信号量，acquire() 获取许可（无许可则阻塞），release() 释放许可。
 * 初始化两个信号量，许可数为 0：
 *   - semC: B 执行完后 release，C acquire 后才能执行
 *   - semA: C 执行完后 release，A acquire 后才能执行
 *
 * 本质就是用"许可"来传递"可以执行了"的信号。
 */
public class SemaphoreDemo {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        Semaphore semC = new Semaphore(0); // C等待B的许可
        Semaphore semA = new Semaphore(0); // A等待C的许可

        Thread threadA = new Thread(() -> {
            int expected = 100;
            try {
                semA.acquire(); // 等C给许可
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
        });

        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
            semC.release(); // 通知C可以执行了
        });

        Thread threadC = new Thread(() -> {
            try {
                semC.acquire(); // 等B给许可
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
            semA.release(); // 通知A可以执行了
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }

//    Semaphore sem = new Semaphore(5); // 最多5个线程同时执行
//
//    // 100个线程都跑这段代码
//    void handleRequest() {
//        sem.acquire();   // 拿到许可才能进，最多5个同时进
//        try {
//            callDatabase(); // 同一时刻最多5个线程在访问数据库
//        } finally {
//            sem.release(); // 用完归还，下一个人可以进
//        }
//    }


}
