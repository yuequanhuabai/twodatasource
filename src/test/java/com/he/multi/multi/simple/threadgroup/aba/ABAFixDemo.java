package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 使用 AtomicStampedReference 解决 ABA 问题
 *
 * 原理：AtomicStampedReference 在值的基础上增加了一个 int 类型的版本号（stamp）。
 * CAS 操作时必须同时满足两个条件才能成功：
 *   1. 当前值 == 期望值
 *   2. 当前版本号 == 期望版本号
 *
 * 这样即使值被 B→C 改回了原值 100，版本号已经从 0 递增到了 2，
 * A 拿着旧版本号 0 去 CAS 就会失败，从而感知到"中间发生过变化"。
 *
 * 对比 Test1：
 *   - Test1 用 AtomicInteger，CAS(100, 50) 成功（ABA 发生，错误的成功）
 *   - 本类用 AtomicStampedReference，CAS(100, 50, stamp=0, stamp+1) 失败（ABA 被检测到）
 */
public class ABAFixDemo {

//    public static void main(String[] args) throws InterruptedException {
//
//        // 初始值 100，初始版本号 0
//        AtomicStampedReference<Integer> balance = new AtomicStampedReference<>(100, 0);
//
//        CountDownLatch latchB = new CountDownLatch(1);
//        CountDownLatch latchC = new CountDownLatch(1);
//
//        Thread threadA = new Thread(() -> {
//            // A 在最开始就读取值和版本号
//            int expectedValue = balance.getReference();
//            int expectedStamp = balance.getStamp();
//            System.out.println("ThreadA: current balance =" + expectedValue + ", current version =" + expectedStamp);
//
//            try {
//                latchC.await(); // 等 B 和 C 都执行完
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            // 此时值已被 B→C 改回 100，但版本号已经变了
//            System.out.println("ThreadA: current balance =" + balance.getReference() + ", current version =" + balance.getStamp());
//
//            boolean success = balance.compareAndSet(expectedValue, expectedValue - 50, expectedStamp, expectedStamp + 1);
//            System.out.println("ThreadA: execute CAS operate: " + (success ? "success" : "failed")
//                    + ", current balance:" + balance.getReference()
//                    + ", stamp:" + balance.getStamp());
//
//            if (!success) {
//                System.out.println("ThreadA: CAS failed! 检测到值在中间被修改过（ABA 被识别）");
//            }
//        });
//
//        Thread threadB = new Thread(() -> {
//            int stamp = balance.getStamp();
//            boolean success = balance.compareAndSet(100, 50, stamp, stamp + 1);
//            System.out.println("ThreadB: modify balance: 100→50, stamp: " + stamp + "→" + (stamp + 1) + ", " + (success ? "success" : "failed"));
//            latchB.countDown();
//        });
//
//        Thread threadC = new Thread(() -> {
//            try {
//                latchB.await(); // 等 B 执行完
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            int stamp = balance.getStamp();
//            boolean success = balance.compareAndSet(50, 100, stamp, stamp + 1);
//            System.out.println("ThreadC: modify balance: 50→100, stamp: " + stamp + "→" + (stamp + 1) + ", " + (success ? "success" : "failed"));
//            latchC.countDown();
//        });
//
//        threadA.start();
//        threadB.start();
//        threadC.start();
//
//        threadA.join();
//        threadB.join();
//        threadC.join();
//    }



    public static void main(String[] args) throws InterruptedException {
        AtomicStampedReference<Integer> balance = new AtomicStampedReference<>(100, 0);

        Thread threadA = new Thread(() -> {
            int[] stampHolder = new int[1];
            balance.get(stampHolder);
            int expectedStamp = stampHolder[0];
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean success = balance.compareAndSet(100, 50, expectedStamp, expectedStamp + 1);
            System.out.println("ThreadA execute CAS operate: " + (success?"success":"failed") + ", current balance is: " + balance.getReference());

        });

        Thread threadB = new Thread(() -> {
            int[] stampHolder = new int[1];
            Integer value = balance.get(stampHolder);
            balance.compareAndSet(100, 50, stampHolder[0], stampHolder[0] + 1);
            System.out.println("ThreadB change balance to 50 ");
        });

        Thread threadC = new Thread(() -> {
            int[] stampHolder = new int[1];
            Integer value = balance.get(stampHolder);
            balance.compareAndSet(100, 50, stampHolder[0], stampHolder[0] + 1);
            System.out.println("ThreadC change balance to 100");
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();

    }
}
