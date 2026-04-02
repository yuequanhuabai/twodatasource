package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.Exchanger;

/**
 * Exchanger 典型场景：两个线程在汇合点交换数据
 * Typical scenario: two threads exchange data at a rendezvous point
 *
 * 本质：一个双向数据交换点。
 * Essence: A bidirectional data exchange point.
 *   - 两个线程各自调用 exchange(data)
 *     Two threads each call exchange(data)
 *   - 先到的线程会阻塞，等另一个线程也到达
 *     The first thread to arrive blocks, waiting for the other thread to arrive
 *   - 两个线程同时到达后，交换彼此的数据
 *     Once both threads arrive simultaneously, they swap each other's data
 *
 * 只能用于两个线程之间，是最简单的线程间数据交换工具。
 * Can only be used between two threads, the simplest inter-thread data exchange tool.
 *
 * 场景：模拟间谍接头，两个特工在秘密地点交换情报。
 * Scenario: Simulate a spy rendezvous, two agents exchange intelligence at a secret location.
 */
public class ExchangerDemo {

    public static void main(String[] args) throws InterruptedException {

        Exchanger<String> exchanger = new Exchanger<>();

        Thread agentA = new Thread(() -> {
            try {
                String myIntel = "Nuclear launch code: 1234";
                System.out.println("Agent-A: carrying intel [" + myIntel + "], heading to rendezvous point...");
                Thread.sleep(1000); // 模拟赶路 / Simulate travel time

                System.out.println("Agent-A: arrived at rendezvous point, waiting for counterpart...");
                // 阻塞，等B来交换
                // Block, wait for B to exchange
                String received = exchanger.exchange(myIntel);

                System.out.println("Agent-A: received counterpart's intel [" + received + "]");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Agent-A");

        Thread agentB = new Thread(() -> {
            try {
                String myIntel = "Satellite orbit params: X=37.5, Y=82.1";
                System.out.println("Agent-B: carrying intel [" + myIntel + "], heading to rendezvous point...");
                Thread.sleep(2000); // B 晚到 / B arrives late

                System.out.println("Agent-B: arrived at rendezvous point, waiting for counterpart...");
                // A 已经在等了，立刻交换
                // A is already waiting, exchange immediately
                String received = exchanger.exchange(myIntel);

                System.out.println("Agent-B: received counterpart's intel [" + received + "]");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Agent-B");

        agentA.start();
        agentB.start();

        agentA.join();
        agentB.join();

        System.out.println("\n=== Rendezvous complete, both agents departed ===");
    }
}
