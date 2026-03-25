package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CyclicBarrier 典型场景：多个线程互相等待，全部到齐后一起出发，可重复使用
 *
 * 本质：一个可循环使用的屏障。
 *   - 每个线程调用 await()，表示"我到了"
 *   - 当所有线程都 await() 后，屏障打开，所有线程同时继续执行
 *   - 屏障打开后自动重置，可以进入下一轮
 *
 * 与 CountDownLatch 的区别：
 *   - CountDownLatch: 一次性的，倒数到 0 后不可重置；主线程等子线程
 *   - CyclicBarrier:  可重复使用；线程之间互相等待，没有主从关系
 *
 * 场景：模拟 3 轮赛跑，每轮 4 名选手准备好后同时起跑。
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) throws InterruptedException {

        int playerCount = 4;
        int rounds = 3;

        // 第二个参数是 barrierAction：所有线程到齐后、放行前执行的回调
        CyclicBarrier barrier = new CyclicBarrier(playerCount, () -> {
            System.out.println(">>> 所有选手已就位，发令枪响！\n");
        });

        for (int i = 1; i <= playerCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    for (int round = 1; round <= rounds; round++) {
                        // 模拟准备时间
                        int prepareMs = ThreadLocalRandom.current().nextInt(200, 800);
                        Thread.sleep(prepareMs);
                        System.out.println("第 " + round + " 轮：选手-" + id + " 准备完毕（耗时 " + prepareMs + "ms），等待其他选手...");

                        barrier.await(); // 等待所有选手就位

                        // 模拟跑步
                        int runMs = ThreadLocalRandom.current().nextInt(100, 500);
                        Thread.sleep(runMs);
                        System.out.println("第 " + round + " 轮：选手-" + id + " 到达终点，用时 " + runMs + "ms");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, "Player-" + id).start();
        }

        // 等子线程执行完（简单做法）
        Thread.sleep(10000);
    }
}
