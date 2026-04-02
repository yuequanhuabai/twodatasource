package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock + Condition 典型场景：多个线程精确交替执行
 * Typical scenario: precise alternating execution of multiple threads
 *
 * 本质：
 * Essence:
 *   - ReentrantLock: 显式锁，比 synchronized 更灵活（可中断、可超时、可公平）
 *     ReentrantLock: explicit lock, more flexible than synchronized (interruptible, timed, fair)
 *   - Condition: Lock 版的 wait/notify，一把锁可以创建多个 Condition（多个等待队列）
 *     Condition: Lock-based wait/notify, one lock can create multiple Conditions (multiple wait queues)
 *
 * 与 synchronized + wait/notify 的区别：
 * Difference from synchronized + wait/notify:
 *   - synchronized 只有一个等待队列，notify() 随机唤醒一个
 *     synchronized has only one wait queue, notify() randomly wakes one thread
 *   - Condition 可以有多个队列，signal() 精确唤醒指定队列中的线程
 *     Condition can have multiple queues, signal() precisely wakes a thread in the specified queue
 *
 * 场景：3 个线程交替打印 A、B、C，循环 5 轮，输出 ABCABCABCABCABC。
 * Scenario: 3 threads alternately print A, B, C for 5 rounds, outputting ABCABCABCABCABC.
 * 每个线程有自己的 Condition，打印完后精确唤醒下一个。
 * Each thread has its own Condition, after printing it precisely wakes the next one.
 */
public class ReentrantLockConditionDemo {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condA = lock.newCondition();
    private static final Condition condB = lock.newCondition();
    private static final Condition condC = lock.newCondition();
    // 0=A的回合, 1=B的回合, 2=C的回合
    // 0=A's turn, 1=B's turn, 2=C's turn
    private static int state = 0;

    public static void main(String[] args) throws InterruptedException {

        int rounds = 5;

        Thread tA = new Thread(() -> {
            for (int i = 0; i < rounds; i++) {
                lock.lock();
                try {
                    // 不是A的回合就等
                    // Not A's turn, wait
                    while (state != 0) condA.await();
                    System.out.print("A");
                    state = 1;
                    // 精确唤醒B
                    // Precisely wake B
                    condB.signal();
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
                    // 精确唤醒C
                    // Precisely wake C
                    condC.signal();
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
                    // 精确唤醒A
                    // Precisely wake A
                    condA.signal();
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

        System.out.println("\n\n=== Printing completed ===");
    }
}
