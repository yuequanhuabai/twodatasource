package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用 CompletableFuture 保证 B → C → A 的严格顺序，稳定复现 ABA 问题
 *
 * 原理：CompletableFuture 支持链式异步编排，thenRun() 保证前一个阶段完成后才执行下一个。
 * 这是最简洁的写法，天然表达了 B → C → A 的顺序。
 */
public class Test9 {

    public static void main(String[] args) {

        AtomicInteger balance = new AtomicInteger(100);

        CompletableFuture.runAsync(() -> {
            // B
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :" + 50);
        }).thenRun(() -> {
            // C
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :" + 100);
        }).thenRun(() -> {
            // A
            int expected = 100;
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed") + ", current balance:" + balance.get());
        }).join(); // 等待整个链执行完毕
    }
}
