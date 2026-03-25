package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CompletableFuture 典型场景：并行调用多个接口，全部返回后合并结果
 *
 * 本质：异步编排框架，支持链式组合、并行扇出、结果合并。
 *   - supplyAsync(): 异步执行有返回值的任务
 *   - thenApply():   前一个完成后，对结果做转换
 *   - thenCombine(): 两个任务都完成后，合并结果
 *   - allOf():       等待所有任务完成
 *   - anyOf():       任意一个完成就返回
 *
 * 场景：电商商品详情页，需要并行查询商品信息、库存、评价，全部返回后组装页面。
 * 如果串行调用总耗时 = 三者之和；并行调用总耗时 = 三者中最慢的那个。
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        // 并行发起三个"接口调用"
        CompletableFuture<String> productFuture = CompletableFuture.supplyAsync(() -> {
            sleep(800);
            return "iPhone 15 Pro";
        });

        CompletableFuture<Integer> stockFuture = CompletableFuture.supplyAsync(() -> {
            sleep(600);
            return 128;
        });

        CompletableFuture<String> reviewFuture = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return "好评率 98%";
        });

        // 三个都完成后，合并结果
        CompletableFuture<String> pageFuture = productFuture
                .thenCombine(stockFuture, (product, stock) ->
                        "商品：" + product + "，库存：" + stock)
                .thenCombine(reviewFuture, (partial, review) ->
                        partial + "，评价：" + review);

        // 获取最终结果
        String page = pageFuture.join();

        long cost = System.currentTimeMillis() - start;
        System.out.println("=== 商品详情页 ===");
        System.out.println(page);
        System.out.println("\n总耗时：" + cost + "ms（三个接口并行，约等于最慢的那个 ~1000ms）");
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
