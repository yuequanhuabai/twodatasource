package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 BlockingQueue 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：SynchronousQueue 是一个容量为 0 的阻塞队列，put() 和 take() 必须配对。
 * put 会阻塞直到另一个线程 take，反之亦然，天然形成一次"握手"。
 *   - B 执行完后 put → C take 后才能执行
 *   - C 执行完后 put → A take 后才能执行
 */
public class Test10 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        BlockingQueue<String> queueBC = new SynchronousQueue<>(); // B→C 的信号通道
        BlockingQueue<String> queueCA = new SynchronousQueue<>(); // C→A 的信号通道

        Thread threadA = new Thread(() -> {
            int expected = 100;
            try {
                queueCA.take(); // 等C的信号
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
                queueBC.put("B done"); // 通知C
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                queueBC.take(); // 等B的信号
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
            try {
                queueCA.put("C done"); // 通知A
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
