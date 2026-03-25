package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 CyclicBarrier 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：CyclicBarrier 让 N 个线程在屏障处互相等待，全部到齐后一起放行。
 * 这里设两个 barrier（parties=2）：
 *   - barrier1: B 完成后与 C 汇合，确保 C 在 B 之后执行
 *   - barrier2: C 完成后与 A 汇合，确保 A 在 C 之后执行
 */
public class Test3 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        CyclicBarrier barrier1 = new CyclicBarrier(2); // B和C之间的屏障
        CyclicBarrier barrier2 = new CyclicBarrier(2); // C和A之间的屏障

        Thread threadA = new Thread(() -> {
            int expected = 100;
            try {
                barrier2.await(); // 等C到达屏障
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
        });

        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
            try {
                barrier1.await(); // 到达屏障，等C来汇合
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                barrier1.await(); // 等B到达屏障
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
            try {
                barrier2.await(); // 到达屏障，等A来汇合
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
