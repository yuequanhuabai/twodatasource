package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue 典型场景：生产者-消费者模式
 * Typical scenario: Producer-Consumer pattern
 *
 * 本质：一个线程安全的队列，自带阻塞能力。
 * Essence: A thread-safe queue with built-in blocking capability.
 *   - put():  队列满时阻塞，直到有空位
 *     put(): blocks when queue is full, until space becomes available
 *   - take(): 队列空时阻塞，直到有数据
 *     take(): blocks when queue is empty, until data becomes available
 *
 * 妙处在于：生产者和消费者完全解耦，不需要手写 wait/notify 或锁。
 * The beauty is: producer and consumer are fully decoupled, no need to manually write wait/notify or locks.
 * 队列本身就是缓冲区，自动协调两边的速度差异。
 * The queue itself acts as a buffer, automatically coordinating speed differences between the two sides.
 *
 * 场景：厨师做菜放到窗口（容量5），服务员从窗口取菜端给客人。
 * Scenario: A chef cooks dishes and places them on a counter (capacity 5), a waiter picks up dishes and serves customers.
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        // 窗口最多放5道菜
        // Counter holds at most 5 dishes
        BlockingQueue<String> window = new ArrayBlockingQueue<>(5);

        // 厨师（生产者）
        // Chef (Producer)
        Thread chef = new Thread(() -> {
            String[] dishes = {"Kung Pao Chicken", "Mapo Tofu", "Twice Cooked Pork", "Fish-flavored Shredded Pork", "Boiled Beef", "Braised Ribs", "Sweet and Sour Pork"};
            try {
                for (String dish : dishes) {
                    Thread.sleep(300); // 模拟做菜时间 / Simulate cooking time
                    window.put(dish);  // 窗口满了就等（阻塞） / Blocks if counter is full
                    System.out.println("Chef: finished [" + dish + "], placed on counter. Counter now has " + window.size() + " dishes");
                }
                window.put("DONE"); // 收工信号 / End-of-work signal
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Chef");

        // 服务员（消费者）
        // Waiter (Consumer)
        Thread waiter = new Thread(() -> {
            try {
                while (true) {
                    // 模拟端菜时间（比做菜慢，窗口会逐渐满）
                    // Simulate serving time (slower than cooking, counter will gradually fill up)
                    Thread.sleep(800);
                    // 窗口空了就等（阻塞）
                    // Blocks if counter is empty
                    String dish = window.take();
                    if ("DONE".equals(dish)) {
                        System.out.println("Waiter: received end-of-work signal, off duty!");
                        break;
                    }
                    System.out.println("Waiter: picked up [" + dish + "]. Counter remaining " + window.size() + " dishes");
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
