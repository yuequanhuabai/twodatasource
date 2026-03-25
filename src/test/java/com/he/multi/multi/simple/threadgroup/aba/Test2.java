package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 CountDownLatch 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：CountDownLatch 是一个倒计数器，await() 会阻塞直到计数减为 0。
 * 设两个 latch：
 *   - latchB: B 执行完后 countDown，C 在 await 后才执行
 *   - latchC: C 执行完后 countDown，A 在 await 后才执行
 */
public class Test2 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        CountDownLatch latchB = new CountDownLatch(1); // B完成后放行C
        CountDownLatch latchC = new CountDownLatch(1); // C完成后放行A

        Thread threadA = new Thread(() -> {
            int expected = 100;
            try {
                latchC.await(); // 等C执行完
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
        });

        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
            latchB.countDown(); // 通知C可以执行了
        });

        Thread threadC = new Thread(() -> {
            try {
                latchB.await(); // 等B执行完
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
            latchC.countDown(); // 通知A可以执行了
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
