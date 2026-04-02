package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Semaphore 典型场景：限流——控制同时访问某个资源的线程数量
 * Typical scenario: rate limiting - control the number of threads accessing a resource concurrently
 *
 * 本质：一个许可计数器。
 * Essence: A permit counter.
 *   - acquire(): 获取一个许可，许可不足则阻塞
 *     acquire(): acquire a permit, blocks if no permits available
 *   - release(): 归还一个许可
 *     release(): return a permit
 *   - 本质上就是控制"同时能有几个线程在干活"
 *     Essentially controls "how many threads can work at the same time"
 *
 * 与锁的区别：
 * Difference from locks:
 *   - 锁是互斥的（同一时刻只有 1 个线程）
 *     Locks are mutual exclusion (only 1 thread at a time)
 *   - Semaphore 是限流的（同一时刻最多 N 个线程）
 *     Semaphore is rate limiting (at most N threads at a time)
 *   - N=1 时 Semaphore 退化为互斥锁
 *     When N=1, Semaphore degrades to a mutex lock
 *
 * 场景：停车场有 3 个车位，6 辆车来抢车位。满了就排队等。
 * Scenario: A parking lot has 3 spots, 6 cars compete for spots. Full lot means waiting in queue.
 */
public class SemaphoreDemo {

    public static void main(String[] args) throws InterruptedException {

        int totalSpots = 3;
        int totalCars = 6;

        // true = 公平模式，先到先得
        // true = fair mode, first come first served
        Semaphore parkingLot = new Semaphore(totalSpots, true);

        System.out.println("=== Parking lot has " + totalSpots + " spots ===\n");

        for (int i = 1; i <= totalCars; i++) {
            final int carId = i;
            new Thread(() -> {
                try {
                    System.out.println("Car-" + carId + " arrived at parking lot, available spots: " + parkingLot.availablePermits());
                    // 获取车位，没有则排队等
                    // Acquire a spot, wait in queue if none available
                    parkingLot.acquire();

                    System.out.println("Car-" + carId + " parked successfully, available spots: " + parkingLot.availablePermits());

                    // 模拟停车时间
                    // Simulate parking duration
                    int parkMs = ThreadLocalRandom.current().nextInt(1000, 3000);
                    Thread.sleep(parkMs);

                    System.out.println("Car-" + carId + " leaving (parked for " + parkMs + "ms), releasing spot");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // 归还车位
                    // Return the spot
                    parkingLot.release();
                }
            }, "Car-" + carId).start();

            // 错开到达时间，方便观察
            // Stagger arrival times for easier observation
            Thread.sleep(200);
        }

        Thread.sleep(10000);
    }
}
