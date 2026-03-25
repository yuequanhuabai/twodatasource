package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 Thread.join() 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：join() 让当前线程阻塞，直到目标线程执行完毕。
 * C 内部 join(B)，A 内部 join(C)，形成 B → C → A 的链式等待。
 *
 * 注意：这种方式本质上把并发变成了串行，仅用于演示，实际生产中不推荐。
 */
public class ThreadJoinDemo {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
        });

        Thread threadC = new Thread(() -> {
            try {
                threadB.join(); // 等B执行完
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
        });

        Thread threadA = new Thread(() -> {
            int expected = 100;
            try {
                threadC.join(); // 等C执行完
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
        });

        threadB.start();
        threadC.start();
        threadA.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
