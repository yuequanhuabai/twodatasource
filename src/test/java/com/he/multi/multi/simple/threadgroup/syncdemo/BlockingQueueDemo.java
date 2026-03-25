package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue 典型场景：生产者-消费者模式
 *
 * 本质：一个线程安全的队列，自带阻塞能力。
 *   - put():  队列满时阻塞，直到有空位
 *   - take(): 队列空时阻塞，直到有数据
 *
 * 妙处在于：生产者和消费者完全解耦，不需要手写 wait/notify 或锁。
 * 队列本身就是缓冲区，自动协调两边的速度差异。
 *
 * 场景：厨师做菜放到窗口（容量5），服务员从窗口取菜端给客人。
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> window = new ArrayBlockingQueue<>(5); // 窗口最多放5道菜

        // 厨师（生产者）
        Thread chef = new Thread(() -> {
            String[] dishes = {"宫保鸡丁", "麻婆豆腐", "回锅肉", "鱼香肉丝", "水煮牛肉", "红烧排骨", "糖醋里脊"};
            try {
                for (String dish : dishes) {
                    Thread.sleep(300); // 模拟做菜时间
                    window.put(dish);  // 窗口满了就等（阻塞）
                    System.out.println("厨师：做好了【" + dish + "】，放到窗口。窗口当前 " + window.size() + " 道菜");
                }
                window.put("DONE"); // 收工信号
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Chef");

        // 服务员（消费者）
        Thread waiter = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(800); // 模拟端菜时间（比做菜慢，窗口会逐渐满）
                    String dish = window.take(); // 窗口空了就等（阻塞）
                    if ("DONE".equals(dish)) {
                        System.out.println("服务员：收到收工信号，下班！");
                        break;
                    }
                    System.out.println("服务员：端走了【" + dish + "】。窗口剩余 " + window.size() + " 道菜");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Waiter");

        chef.start();
        waiter.start();

        chef.join();
        waiter.join();
    }
}
