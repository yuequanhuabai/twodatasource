package com.he.multi.multi.simple.threadgroup.aba;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ### 题目 1.5 ⭐⭐ 中等
 * **ABA问题演示与解决**
 * <p>
 * 1. 编写代码演示ABA问题的发生场景
 * 2. 使用 `AtomicStampedReference` 解决ABA问题
 * <p>
 * 场景：模拟银行账户余额修改
 * - 线程A读取余额100，准备扣款50
 * - 线程B将余额从100改为50
 * - 线程C将余额从50改为100
 * - 线程A执行CAS(100, 50)会成功（但这是错误的！因为中间发生过变化）
 */
public class Test1 {

    public static void main(String[] args) throws InterruptedException {

        AtomicInteger balance = new AtomicInteger(100);

        Thread threadA = new Thread(() -> {
            int expected = 100;
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            boolean success = balance.compareAndSet(expected, expected - 50);
            System.out.println("ThreadA: execute CAS operate: "+ (success?"success":"failed")+", current balance:"+balance.get());
        });


        Thread threadB = new Thread(() -> {
            balance.compareAndSet(100, 50);
            System.out.println("ThreadB: modify balance :"+50);
        });


        Thread threadC = new Thread(() -> {
            balance.compareAndSet(50, 100);
            System.out.println("ThreadC: modify balance :"+100);
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();
    }
}
