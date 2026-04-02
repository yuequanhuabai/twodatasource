package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CyclicBarrier 典型场景：多个线程互相等待，全部到齐后一起出发，可重复使用
 * Typical scenario: multiple threads wait for each other, all proceed together once everyone arrives, reusable
 *
 * 本质：一个可循环使用的屏障。
 * Essence: A reusable barrier.
 *   - 每个线程调用 await()，表示"我到了"
 *     Each thread calls await(), meaning "I'm ready"
 *   - 当所有线程都 await() 后，屏障打开，所有线程同时继续执行
 *     When all threads have called await(), the barrier opens and all threads continue simultaneously
 *   - 屏障打开后自动重置，可以进入下一轮
 *     The barrier resets automatically after opening, ready for the next round
 *
 * 与 CountDownLatch 的区别：
 * Difference from CountDownLatch:
 *   - CountDownLatch: 一次性的，倒数到 0 后不可重置；主线程等子线程
 *     CountDownLatch: one-time use, cannot be reset after reaching 0; main thread waits for child threads
 *   - CyclicBarrier:  可重复使用；线程之间互相等待，没有主从关系
 *     CyclicBarrier: reusable; threads wait for each other, no master-slave relationship
 *
 * 场景：模拟 3 轮赛跑，每轮 4 名选手准备好后同时起跑。
 * Scenario: Simulate 3 rounds of racing, 4 players start together after all are ready in each round.
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) throws InterruptedException {

        int playerCount = 4;
        int rounds = 3;

        // 第二个参数是 barrierAction：所有线程到齐后、放行前执行的回调
        // Second parameter is barrierAction: callback executed after all threads arrive, before releasing
        CyclicBarrier barrier = new CyclicBarrier(playerCount, () -> {
            System.out.println(">>> All players are ready, starting gun fired!\n");
        });

        for (int i = 1; i <= playerCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    for (int round = 1; round <= rounds; round++) {
                        // 模拟准备时间
                        // Simulate preparation time
                        int prepareMs = ThreadLocalRandom.current().nextInt(200, 800);
                        Thread.sleep(prepareMs);
                        System.out.println("Round " + round + ": Player-" + id + " ready (cost " + prepareMs + "ms), waiting for others...");

                        // 等待所有选手就位
                        // Wait for all players to be ready
                        barrier.await();

                        // 模拟跑步
                        // Simulate running
                        int runMs = ThreadLocalRandom.current().nextInt(100, 500);
                        Thread.sleep(runMs);
                        System.out.println("Round " + round + ": Player-" + id + " reached finish line, time " + runMs + "ms");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, "Player-" + id).start();
        }

        // 等子线程执行完（简单做法）
        // Wait for child threads to finish (simple approach)
        Thread.sleep(10000);
    }
}
