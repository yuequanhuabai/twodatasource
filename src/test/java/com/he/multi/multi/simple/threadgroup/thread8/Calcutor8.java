package com.he.multi.multi.simple.threadgroup.thread8;

import java.util.concurrent.atomic.LongAdder;

public class Calcutor8 {
    private LongAdder count = new LongAdder();

    public void increment() {
        count.increment();
    }

    public long getCount() {
        return count.sum();
    }
}
