package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 典型场景：精确控制线程的挂起与唤醒
 * Typical scenario: precise control of thread suspension and wakeup
 *
 * 本质：线程级别的阻塞/唤醒原语，底层基于 Unsafe 的 park/unpark。
 * Essence: Thread-level blocking/wakeup primitive, based on Unsafe's park/unpark underneath.
 *   - park():    挂起当前线程（消耗一个许可）
 *     park(): suspend current thread (consumes one permit)
 *   - unpark(t): 给指定线程 t 发放一个许可（如果 t 已 park，则唤醒它）
 *     unpark(t): grant one permit to thread t (if t is parked, wake it up)
 *
 * 与 wait/notify 的关键区别：
 * Key differences from wait/notify:
 *   1. 不需要持有锁，可以在任何地方调用
 *      No need to hold a lock, can be called anywhere
 *   2. 可以精确唤醒指定线程（不是随机唤醒）
 *      Can precisely wake a specific thread (not random wakeup)
 *   3. 基于"许可"机制：unpark 可以先于 park 调用，不会丢失唤醒
 *      Based on "permit" mechanism: unpark can be called before park, wakeup won't be lost
 *      （而 notify 先于 wait 调用，wait 会永久阻塞）
 *      (whereas notify before wait causes wait to block forever)
 *
 * 场景：模拟接力赛跑，4 个选手依次传递接力棒，每人跑完后精确唤醒下一个选手。
 * Scenario: Simulate a relay race, 4 runners pass the baton in sequence, each precisely wakes the next runner after finishing.
 */
public class LockSupportDemo {

    public static void main(String[] args) throws InterruptedException {

        Thread[] runners = new Thread[4];

        for (int i = 0; i < runners.length; i++) {
            final int leg = i + 1;
            final int idx = i;
            runners[i] = new Thread(() -> {
                if (leg != 1) {
                    // 非第一棒，等待上一个选手传棒
                    // Not the first leg, wait for previous runner to pass the baton
                    LockSupport.park();
                }

                System.out.println("Leg " + leg + " runner starts running...");
                try {
                    Thread.sleep(500); // 模拟跑步 / Simulate running
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Leg " + leg + " runner finished!");

                // 传棒给下一个选手
                // Pass the baton to the next runner
                if (idx + 1 < runners.length) {
                    System.out.println("-> Passing baton to leg " + (leg + 1) + "\n");
                    // 精确唤醒下一个
                    // Precisely wake the next runner
                    LockSupport.unpark(runners[idx + 1]);
                } else {
                    System.out.println("\n=== Relay race completed! ===");
                }
            }, "Runner-" + leg);
        }

        // 先全部 start（2/3/4 棒会立刻 park 住）
        // Start all threads (legs 2/3/4 will park immediately)
        for (Thread runner : runners) {
            runner.start();
        }

        // 等全部跑完
        // Wait for all to finish
        for (Thread runner : runners) {
            runner.join();
        }
    }
}
