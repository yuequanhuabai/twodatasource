package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CountDownLatch 典型场景：主线程等待 N 个子线程全部完成后，再汇总结果
 * Typical scenario: main thread waits for N child threads to complete, then aggregates results
 *
 * 本质：一个一次性的倒计数器。
 * Essence: A one-time countdown counter.
 *   - 初始化计数为 N
 *     Initialize count to N
 *   - 每个子线程完成后调用 countDown()，计数减 1
 *     Each child thread calls countDown() upon completion, decrementing count by 1
 *   - 主线程调用 await()，阻塞直到计数减为 0
 *     Main thread calls await(), blocking until count reaches 0
 *   - 不可重置，用完即废
 *     Cannot be reset, single-use only
 *
 * 场景：模拟 5 个质检员分别检查不同产品，全部检查完毕后主线程汇总报告。
 * Scenario: Simulate 5 inspectors checking different products; main thread generates summary report after all finish.
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {

        int inspectorCount = 5;
        CountDownLatch latch = new CountDownLatch(inspectorCount);

        System.out.println("=== Inspection started, total " + inspectorCount + " inspectors ===\n");

        for (int i = 1; i <= inspectorCount; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    int costMs = ThreadLocalRandom.current().nextInt(500, 2000);
                    Thread.sleep(costMs);
                    System.out.println("Inspector-" + id + " finished inspection, cost " + costMs + "ms");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // 无论成功失败，都要 countDown，否则主线程永远阻塞
                    // Always countDown regardless of success/failure, otherwise main thread blocks forever
                    latch.countDown();
                }
            }, "Inspector-" + id).start();
        }

        // 主线程阻塞，等所有质检员完成
        // Main thread blocks, waiting for all inspectors to finish
        latch.await();

        System.out.println("\n=== All inspections completed, generating summary report ===");
    }
}
