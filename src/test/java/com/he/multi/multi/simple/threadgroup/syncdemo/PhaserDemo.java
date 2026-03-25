package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Phaser 典型场景：多阶段任务，每个阶段所有参与者完成后才进入下一阶段
 *
 * 本质：CountDownLatch + CyclicBarrier 的增强版。
 *   - 支持多阶段（phase 0, 1, 2...），每个阶段结束自动推进
 *   - 支持动态注册/注销参与者（register/deregister）
 *   - 可重写 onAdvance() 控制是否继续下一阶段
 *
 * 与 CyclicBarrier 的区别：
 *   - CyclicBarrier 参与者数量固定
 *   - Phaser 可以动态增减参与者，更灵活
 *
 * 场景：模拟考试，3 个学生分 3 个阶段（笔试 → 面试 → 体检），每个阶段必须所有人完成后才进入下一阶段。
 */
public class PhaserDemo {

    public static void main(String[] args) throws InterruptedException {

        String[] phases = {"笔试", "面试", "体检"};
        int studentCount = 3;

        // 重写 onAdvance：每个阶段结束时打印信息
        Phaser phaser = new Phaser(studentCount) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                if (phase < phases.length) {
                    System.out.println("\n=== 第 " + (phase + 1) + " 阶段【" + phases[phase] + "】全部完成 ===\n");
                }
                // 返回 true 表示 phaser 终止，false 表示继续
                return phase >= phases.length - 1;
            }
        };

        for (int i = 1; i <= studentCount; i++) {
            final int id = i;
            new Thread(() -> {
                for (int phase = 0; phase < phases.length; phase++) {
                    // 模拟每个阶段的耗时
                    int costMs = ThreadLocalRandom.current().nextInt(500, 1500);
                    try {
                        Thread.sleep(costMs);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("学生-" + id + " 完成【" + phases[phase] + "】，耗时 " + costMs + "ms");
                    phaser.arriveAndAwaitAdvance(); // 等待其他学生完成本阶段
                }
            }, "Student-" + id).start();
        }

        Thread.sleep(15000);
    }
}
