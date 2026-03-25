package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.Exchanger;

/**
 * Exchanger 典型场景：两个线程在汇合点交换数据
 *
 * 本质：一个双向数据交换点。
 *   - 两个线程各自调用 exchange(data)
 *   - 先到的线程会阻塞，等另一个线程也到达
 *   - 两个线程同时到达后，交换彼此的数据
 *
 * 只能用于两个线程之间，是最简单的线程间数据交换工具。
 *
 * 场景：模拟间谍接头，两个特工在秘密地点交换情报。
 */
public class ExchangerDemo {

    public static void main(String[] args) throws InterruptedException {

        Exchanger<String> exchanger = new Exchanger<>();

        Thread agentA = new Thread(() -> {
            try {
                String myIntel = "核弹发射密码: 1234";
                System.out.println("特工A：携带情报【" + myIntel + "】，前往接头地点...");
                Thread.sleep(1000); // 模拟赶路

                System.out.println("特工A：到达接头地点，等待对方...");
                String received = exchanger.exchange(myIntel); // 阻塞，等B来交换

                System.out.println("特工A：收到对方情报【" + received + "】");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Agent-A");

        Thread agentB = new Thread(() -> {
            try {
                String myIntel = "卫星轨道参数: X=37.5, Y=82.1";
                System.out.println("特工B：携带情报【" + myIntel + "】，前往接头地点...");
                Thread.sleep(2000); // B 晚到

                System.out.println("特工B：到达接头地点，等待对方...");
                String received = exchanger.exchange(myIntel); // A 已经在等了，立刻交换

                System.out.println("特工B：收到对方情报【" + received + "】");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Agent-B");

        agentA.start();
        agentB.start();

        agentA.join();
        agentB.join();

        System.out.println("\n=== 接头完成，双方各自离开 ===");
    }
}
