package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 Phaser 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：Phaser 是多阶段同步器，支持多个阶段（phase），每个阶段所有注册方到齐后才推进到下一阶段。
 *   - phase 0: B 执行完后 arrive，C 和 A 等待
 *   - phase 1: C 执行完后 arrive，A 等待
 *   - phase 2: A 执行
 *
 * 3个线程注册到同一个 Phaser，通过 arriveAndAwaitAdvance() 逐阶段推进。
 */
public class Test5 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        // 注册3个参与方
        Phaser phaser = new Phaser(3);

        Thread threadA = new Thread(() -> {
            int expected = 100;
            phaser.arriveAndAwaitAdvance(); // 等待phase 0结束（B执行完）
            phaser.arriveAndAwaitAdvance(); // 等待phase 1结束（C执行完）
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
            phaser.arriveAndDeregister();   // A完成，注销
        });

        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
            phaser.arriveAndAwaitAdvance(); // phase 0完成
            phaser.arriveAndDeregister();   // B后续不参与，注销
        });

        Thread threadC = new Thread(() -> {
            phaser.arriveAndAwaitAdvance(); // 等待phase 0结束（B执行完）
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
            phaser.arriveAndAwaitAdvance(); // phase 1完成
            phaser.arriveAndDeregister();   // C完成，注销
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
