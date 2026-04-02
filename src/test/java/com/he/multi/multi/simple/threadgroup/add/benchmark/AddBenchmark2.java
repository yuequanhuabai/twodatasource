package com.he.multi.multi.simple.threadgroup.add.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 使用 JMH(Java Microbenchmark Harness) 对比 AtomicLong 与 LongAdder 在高并发累加场景下的性能。
 *
 * 关键注解说明：
 * - @Fork(2)        : 启动 2 个独立 JVM 进程，消除进程间交叉影响
 * - @Warmup          : 预热 3 轮，让 JIT 充分编译热点代码后再计分
 * - @Measurement      : 正式测量 5 轮，取平均值
 * - @Threads(100)     : 模拟 100 个线程并发调用被测方法
 * - @State(Scope.Benchmark) : 所有线程共享同一个 state 实例（即同一个计数器）
 */
@BenchmarkMode(Mode.Throughput)            // 测吞吐量：单位时间内完成多少次操作
@OutputTimeUnit(TimeUnit.MILLISECONDS)     // 结果单位：ops/ms
@Fork(2)                                   // 2 个独立 JVM 进程
@Warmup(iterations = 3, time = 1)          // 预热 3 轮，每轮 1 秒
@Measurement(iterations = 5, time = 1)     // 正式测量 5 轮，每轮 1 秒
@Threads(100)                              // 100 线程并发
@State(Scope.Benchmark)                    // 所有线程共享状态
public class AddBenchmark2 {

    private AtomicLong atomicLong;
    private LongAdder longAdder;

    /**
     * 每轮迭代前重置计数器，保证各轮之间互不影响。
     */
    @Setup(Level.Iteration)
    public void setup() {
        atomicLong = new AtomicLong(0);
        longAdder = new LongAdder();
    }

    @Benchmark
    public long testAtomicLong() {
        // 返回值会被 JMH 自动通过 Blackhole 消费，防止 JIT 死代码消除
        return atomicLong.incrementAndGet();
    }

    @Benchmark
    public void testLongAdder() {
        // LongAdder.increment() 返回 void，JMH 对 void 方法天然不会被 DCE
        longAdder.increment();
    }

    /**
     * 通过 main 方法启动 JMH，也可以用 mvn 命令行启动。
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AddBenchmark2.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
