package com.he.multi.multi.simple.threadgroup.cas.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

// 需要使用jdk8環境去運行代碼
public class MyCASCounter {
    private volatile int value;

    private static final Unsafe UNSAFE;

    private static final long VALUE_OFFSET;

    static {
        try {
            // Unsafe 構造函數是私有的，只能通過反射拿到
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);

            // 拿到 value字段在對象中的內存偏移量
            VALUE_OFFSET = UNSAFE.objectFieldOffset(MyCASCounter.class.getDeclaredField("value"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int incrementAndGet() {
        for (; ; ) {
            int old = value;
            int next = old + 1;
            if (UNSAFE.compareAndSwapInt(this, VALUE_OFFSET, old, next)) {
                return next;
            }
        }
    }

    public int get() {
        return value;
    }

}
