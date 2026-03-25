package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 用 LockSupport.park()/unpark() 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：LockSupport 提供线程级别的阻塞/唤醒能力，不需要持有锁。
 *   - park()  : 阻塞当前线程
 *   - unpark(t): 唤醒指定线程 t
 *
 * 与 wait/notify 的区别：
 *   1. 不需要在 synchronized 块中使用
 *   2. 可以精确唤醒某个线程（unpark 指定目标）
 *   3. unpark 可以先于 park 调用（有"许可"机制，不会丢失唤醒）
 */
public class Test8 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        Thread threadA = new Thread(() -> {
            int expected = 100;
            LockSupport.park(); // 阻塞，等C唤醒
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
        });

        Thread threadC = new Thread(() -> {
            LockSupport.park(); // 阻塞，等B唤醒
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
            LockSupport.unpark(threadA); // 唤醒A
        });

        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
            LockSupport.unpark(threadC); // 唤醒C
        });

        threadA.start();
        threadC.start();
        threadB.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
