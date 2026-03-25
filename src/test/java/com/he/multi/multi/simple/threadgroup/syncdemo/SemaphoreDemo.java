package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Semaphore 典型场景：限流——控制同时访问某个资源的线程数量
 *
 * 本质：一个许可计数器。
 *   - acquire(): 获取一个许可，许可不足则阻塞
 *   - release(): 归还一个许可
 *   - 本质上就是控制"同时能有几个线程在干活"
 *
 * 与锁的区别：
 *   - 锁是互斥的（同一时刻只有 1 个线程）
 *   - Semaphore 是限流的（同一时刻最多 N 个线程）
 *   - N=1 时 Semaphore 退化为互斥锁
 *
 * 场景：停车场有 3 个车位，6 辆车来抢车位。满了就排队等。
 */
public class SemaphoreDemo {

    public static void main(String[] args) throws InterruptedException {

        int totalSpots = 3;
        int totalCars = 6;

        Semaphore parkingLot = new Semaphore(totalSpots, true); // true = 公平模式，先到先得

        System.out.println("=== 停车场共 " + totalSpots + " 个车位 ===\n");

        for (int i = 1; i <= totalCars; i++) {
            final int carId = i;
            new Thread(() -> {
                try {
                    System.out.println("车-" + carId + " 到达停车场，剩余车位：" + parkingLot.availablePermits());
                    parkingLot.acquire(); // 获取车位，没有则排队等

                    System.out.println("车-" + carId + " 停入车位 ✓，剩余车位：" + parkingLot.availablePermits());

                    // 模拟停车时间
                    int parkMs = ThreadLocalRandom.current().nextInt(1000, 3000);
                    Thread.sleep(parkMs);

                    System.out.println("车-" + carId + " 驶离（停了 " + parkMs + "ms），释放车位");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    parkingLot.release(); // 归还车位
                }
            }, "Car-" + carId).start();

            Thread.sleep(200); // 错开到达时间，方便观察
        }

        Thread.sleep(10000);
    }
}
