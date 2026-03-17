package com.he.multi.multi.simple.threadgroup.thread8;

import java.util.concurrent.atomic.LongAdder;

public class Calcutor8 {
    private LongAdder count = new LongAdder();

    public void increment() {
        count.increment();
    }

    public long getCount() {
        // 为什么不用initValue而是sum，因为initValue的底层是sum，而且还会把long强转位int;会有精度丢失的风险;
//        return count.intValue();
        return count.sum();

    }
}
