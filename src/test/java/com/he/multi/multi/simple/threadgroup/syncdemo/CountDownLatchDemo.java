package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CountDownLatch 典型场景：主线程等待 N 个子线程全部完成后，再汇总结果
 *
 * 本质：一个一次性的倒计数器。
 *   - 初始化计数为 N
 *   - 每个子线程完成后调用 countDown()，计数减 1
 *   - 主线程调用 await()，阻塞直到计数减为 0
 *   - 不可重置，用完即废
 *
 * 场景：模拟 5 个质检员分别检查不同产品，全部检查完毕后主线程汇总报告。
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {

        int inspectorCount = 5;
        CountDownLatch latch = new CountDownLatch(inspectorCount);

        System.out.println("=== 质检开始，共 " + inspectorCount + " 名质检员 ===\n");

        for (int i = 1; i <= inspectorCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    int costMs = ThreadLocalRandom.current().nextInt(500, 2000);
                    Thread.sleep(costMs);
                    System.out.println("质检员-" + id + " 检查完毕，耗时 " + costMs + "ms");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown(); // 无论成功失败，都要 countDown，否则主线程永远阻塞
                }
            }, "Inspector-" + id).start();
        }

        latch.await(); // 主线程阻塞，等所有质检员完成

        System.out.println("\n=== 全部检查完毕，生成汇总报告 ===");
    }
}
