package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock + Condition 典型场景：多个线程精确交替执行
 *
 * 本质：
 *   - ReentrantLock: 显式锁，比 synchronized 更灵活（可中断、可超时、可公平）
 *   - Condition: Lock 版的 wait/notify，一把锁可以创建多个 Condition（多个等待队列）
 *
 * 与 synchronized + wait/notify 的区别：
 *   - synchronized 只有一个等待队列，notify() 随机唤醒一个
 *   - Condition 可以有多个队列，signal() 精确唤醒指定队列中的线程
 *
 * 场景：3 个线程交替打印 A、B、C，循环 5 轮，输出 ABCABCABCABCABC。
 * 每个线程有自己的 Condition，打印完后精确唤醒下一个。
 */
public class ReentrantLockConditionDemo {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condA = lock.newCondition();
    private static final Condition condB = lock.newCondition();
    private static final Condition condC = lock.newCondition();
    private static int state = 0; // 0=A的回合, 1=B的回合, 2=C的回合

    public static void main(String[] args) throws InterruptedException {

        int rounds = 5;

        Thread tA = new Thread(() -> {
            for (int i = 0; i < rounds; i++) {
                lock.lock();
                try {
                    while (state != 0) condA.await();  // 不是A的回合就等
                    System.out.print("A");
                    state = 1;
                    condB.signal(); // 精确唤醒B
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }, "Thread-A");

        Thread tB = new Thread(() -> {
            for (int i = 0; i < rounds; i++) {
                lock.lock();
                try {
                    while (state != 1) condB.await();
                    System.out.print("B");
                    state = 2;
                    condC.signal(); // 精确唤醒C
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }, "Thread-B");

        Thread tC = new Thread(() -> {
            for (int i = 0; i < rounds; i++) {
                lock.lock();
                try {
                    while (state != 2) condC.await();
                    System.out.print("C");
                    state = 0;
                    condA.signal(); // 精确唤醒A
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }, "Thread-C");

        tA.start();
        tB.start();
        tC.start();

        tA.join();
        tB.join();
        tC.join();

        System.out.println("\n\n=== 打印完毕 ===");
    }
}
