package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用 ReentrantLock + Condition 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：Condition 是 Lock 版的 wait/notify，可以创建多个等待队列。
 * 用一个状态变量 step 标记当前该谁执行：
 *   - step=0: B 执行
 *   - step=1: C 执行
 *   - step=2: A 执行
 * 每个线程在 while 循环中检查 step，不是自己的回合就 await()。
 */
public class ReentrantLockConditionDemo {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static int step = 0;

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        Thread threadA = new Thread(() -> {
            int expected = 100;
            lock.lock();
            try {
                while (step != 2) {
                    condition.await(); // 不是A的回合，等待
                }
                boolean success = balance.compareAndSet(expected, expected - 50);
                System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        Thread threadB = new Thread(() -> {
            lock.lock();
            try {
                while (step != 0) {
                    condition.await(); // 不是B的回合，等待
                }
                balance.compareAndSet(100, 50);
                System.out.println("ThreadB: modify balance :" + 50);
                step = 1;
                condition.signalAll(); // 唤醒所有等待的线程
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        Thread threadC = new Thread(() -> {
            lock.lock();
            try {
                while (step != 1) {
                    condition.await(); // 不是C的回合，等待
                }
                balance.compareAndSet(50, 100);
                System.out.println("ThreadC: modify balance :" + 100);
                step = 2;
                condition.signalAll(); // 唤醒所有等待的线程
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }

//    它不是用来做"排队轮流执行"的，而是用来做精细化的条件等待。
//
//    典型场景：生产者-消费者（有界缓冲区）
//
//    ReentrantLock lock = new ReentrantLock();
//    Condition notFull  = lock.newCondition(); // 队列没满，生产者可以放
//    Condition notEmpty = lock.newCondition(); // 队列没空，消费者可以取
//
//    // 生产者
//  lock.lock();
//  try {
//        while (queue.size() == MAX) {
//            notFull.await();        // 满了，等消费者取走
//        }
//        queue.add(item);
//        notEmpty.signal();          // 放了一个，通知消费者来取
//    } finally {
//        lock.unlock();
//    }
//
//    // 消费者
//  lock.lock();
//  try {
//        while (queue.isEmpty()) {
//            notEmpty.await();       // 空了，等生产者放入
//        }
//        item = queue.poll();
//        notFull.signal();           // 取了一个，通知生产者可以放
//    } finally {
//        lock.unlock();
//    }


}
