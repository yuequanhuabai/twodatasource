package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CompletableFuture 典型场景：并行调用多个接口，全部返回后合并结果
 * Typical scenario: call multiple APIs in parallel, merge results after all return
 *
 * 本质：异步编排框架，支持链式组合、并行扇出、结果合并。
 * Essence: An async orchestration framework supporting chained composition, parallel fan-out, and result merging.
 *   - supplyAsync(): 异步执行有返回值的任务
 *     supplyAsync(): asynchronously execute a task with return value
 *   - thenApply():   前一个完成后，对结果做转换
 *     thenApply(): transform the result after the previous task completes
 *   - thenCombine(): 两个任务都完成后，合并结果
 *     thenCombine(): merge results after both tasks complete
 *   - allOf():       等待所有任务完成
 *     allOf(): wait for all tasks to complete
 *   - anyOf():       任意一个完成就返回
 *     anyOf(): return as soon as any one task completes
 *
 * 场景：电商商品详情页，需要并行查询商品信息、库存、评价，全部返回后组装页面。
 * Scenario: E-commerce product detail page needs to query product info, inventory, and reviews in parallel,
 * 如果串行调用总耗时 = 三者之和；并行调用总耗时 = 三者中最慢的那个。
 * then assemble the page. Serial total time = sum of all three; parallel total time = the slowest one.
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        // 并行发起三个"接口调用"
        // Launch three "API calls" in parallel
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
            return "98% positive reviews";
        });

        // 三个都完成后，合并结果
        // After all three complete, merge results
        CompletableFuture<String> pageFuture = productFuture
                .thenCombine(stockFuture, (product, stock) ->
                        "Product: " + product + ", Stock: " + stock)
                .thenCombine(reviewFuture, (partial, review) ->
                        partial + ", Reviews: " + review);

        // 获取最终结果
        // Get the final result
        String page = pageFuture.join();

        long cost = System.currentTimeMillis() - start;
        System.out.println("=== Product Detail Page ===");
        System.out.println(page);
        System.out.println("\nTotal time: " + cost + "ms (three APIs in parallel, approximately the slowest ~1000ms)");
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
