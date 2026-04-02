package com.he.multi.multi.simple.threadgroup.syncdemo;

import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Phaser 典型场景：多阶段任务，每个阶段所有参与者完成后才进入下一阶段
 * Typical scenario: multi-phase tasks, all participants must complete each phase before advancing to the next
 *
 * 本质：CountDownLatch + CyclicBarrier 的增强版。
 * Essence: Enhanced version of CountDownLatch + CyclicBarrier.
 *   - 支持多阶段（phase 0, 1, 2...），每个阶段结束自动推进
 *     Supports multiple phases (phase 0, 1, 2...), automatically advances after each phase ends
 *   - 支持动态注册/注销参与者（register/deregister）
 *     Supports dynamic registration/deregistration of participants (register/deregister)
 *   - 可重写 onAdvance() 控制是否继续下一阶段
 *     Can override onAdvance() to control whether to continue to the next phase
 *
 * 与 CyclicBarrier 的区别：
 * Difference from CyclicBarrier:
 *   - CyclicBarrier 参与者数量固定
 *     CyclicBarrier has a fixed number of participants
 *   - Phaser 可以动态增减参与者，更灵活
 *     Phaser allows dynamic add/remove of participants, more flexible
 *
 * 场景：模拟考试，3 个学生分 3 个阶段（笔试 → 面试 → 体检），每个阶段必须所有人完成后才进入下一阶段。
 * Scenario: Simulate an exam, 3 students go through 3 phases (Written Test -> Interview -> Physical Exam),
 * all must complete each phase before advancing to the next.
 */
public class PhaserDemo {

    public static void main(String[] args) throws InterruptedException {

        String[] phases = {"Written Test", "Interview", "Physical Exam"};
        int studentCount = 3;

        // 重写 onAdvance：每个阶段结束时打印信息
        // Override onAdvance: print info at the end of each phase
        Phaser phaser = new Phaser(studentCount) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                if (phase < phases.length) {
                    System.out.println("\n=== Phase " + (phase + 1) + " [" + phases[phase] + "] all completed ===\n");
                }
                // 返回 true 表示 phaser 终止，false 表示继续
                // Return true to terminate phaser, false to continue
                return phase >= phases.length - 1;
            }
        };

        for (int i = 1; i <= studentCount; i++) {
            final int id = i;
            new Thread(() -> {
                for (int phase = 0; phase < phases.length; phase++) {
                    // 模拟每个阶段的耗时
                    // Simulate time spent on each phase
                    int costMs = ThreadLocalRandom.current().nextInt(500, 1500);
                    try {
                        Thread.sleep(costMs);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Student-" + id + " completed [" + phases[phase] + "], cost " + costMs + "ms");
                    // 等待其他学生完成本阶段
                    // Wait for other students to complete this phase
                    phaser.arriveAndAwaitAdvance();
                }
            }, "Student-" + id).start();
        }

        Thread.sleep(15000);
    }
}
