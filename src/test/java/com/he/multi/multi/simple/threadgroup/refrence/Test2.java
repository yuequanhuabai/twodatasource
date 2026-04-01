package com.he.multi.multi.simple.threadgroup.refrence;


import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Test2 {

    // 多线程的CyclicBarrier的实际应用
    static AtomicInteger totalScore = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier barrier = new CyclicBarrier(4, () -> {
            // 凑齐4人后，由最后到达的线程执行
            System.out.println("── 本轮全部完成，当前总分: " + totalScore.get() + " ──\n");
        });

        for (int i = 1; i <= 4; i++) {
            final int playerId = i;
            new Thread(() -> {
                try {
                    for (int round = 1; round <= 3; round++) {
                        int score = playerId * 10; // 模拟每人得分
                        totalScore.addAndGet(score);
                        System.out.println("第" + round + "轮: 玩家" + playerId + " 得分 " + score);

                        barrier.await(); // 等其他人都算完这一轮
                        // ↑ 凑齐4人后:
                        //   1. 先执行 barrierAction（打印总分）
                        //   2. 然后4个线程一起放行，进入下一轮
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }


    private static void change(int[] arr) {
        arr = new int[]{999};
    }
}
