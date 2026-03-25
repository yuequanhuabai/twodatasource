package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 典型场景：精确控制线程的挂起与唤醒
 *
 * 本质：线程级别的阻塞/唤醒原语，底层基于 Unsafe 的 park/unpark。
 *   - park():    挂起当前线程（消耗一个许可）
 *   - unpark(t): 给指定线程 t 发放一个许可（如果 t 已 park，则唤醒它）
 *
 * 与 wait/notify 的关键区别：
 *   1. 不需要持有锁，可以在任何地方调用
 *   2. 可以精确唤醒指定线程（不是随机唤醒）
 *   3. 基于"许可"机制：unpark 可以先于 park 调用，不会丢失唤醒
 *      （而 notify 先于 wait 调用，wait 会永久阻塞）
 *
 * 场景：模拟接力赛跑，4 个选手依次传递接力棒，每人跑完后精确唤醒下一个选手。
 */
public class LockSupportDemo {

    public static void main(String[] args) throws InterruptedException {

        Thread[] runners = new Thread[4];

        for (int i = 0; i < runners.length; i++) {
            final int leg = i + 1;
            final int idx = i;
            runners[i] = new Thread(() -> {
                if (leg != 1) {
                    LockSupport.park(); // 非第一棒，等待上一个选手传棒
                }

                System.out.println("第 " + leg + " 棒选手开始跑...");
                try {
                    Thread.sleep(500); // 模拟跑步
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("第 " + leg + " 棒选手跑完！");

                // 传棒给下一个选手
                if (idx + 1 < runners.length) {
                    System.out.println("→ 传棒给第 " + (leg + 1) + " 棒\n");
                    LockSupport.unpark(runners[idx + 1]); // 精确唤醒下一个
                } else {
                    System.out.println("\n=== 接力赛完成！ ===");
                }
            }, "Runner-" + leg);
        }

        // 先全部 start（2/3/4 棒会立刻 park 住）
        for (Thread runner : runners) {
            runner.start();
        }

        // 等全部跑完
        for (Thread runner : runners) {
            runner.join();
        }
    }
}
