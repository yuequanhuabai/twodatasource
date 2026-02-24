package com.he.multi.multi.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SumTask implements Callable<Long> {
    private final int start;
    private final int end;

    public SumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long call() {
        long sum = 0;
        for (int i = start; i <= end; i++) {
            sum += i;
        }
        System.out.println(Thread.currentThread().getName() + " 计算 " + start + "~" + end + " = " + sum);
        return sum;
    }

    public static void main(String[] args) throws Exception {
        int total = 10000;
        int threadCount = 10;
        int range = total / threadCount;

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        List<Future<Long>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int start = i * range + 1;
            int end = (i + 1) * range;
            futures.add(pool.submit(new SumTask(start, end)));
        }

        long result = 0;
        for (Future<Long> f : futures) {
            result += f.get();
        }

        System.out.println("最终结果: " + result);
        System.out.println("验证: " + (long) (1 + total) * total / 2);

        pool.shutdown();
    }
}
