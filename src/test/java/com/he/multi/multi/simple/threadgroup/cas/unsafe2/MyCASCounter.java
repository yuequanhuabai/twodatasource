package com.he.multi.multi.simple.threadgroup.cas.unsafe2;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

// ��Ҫʹ��jdk8�h��ȥ�\�д��a
public class MyCASCounter {
    private volatile int value;

    private static final Unsafe UNSAFE;

    private static final long VALUE_OFFSET;

    static {
        try {
            // Unsafe ���캯����˽�еģ�ֻ��ͨ�^�����õ�
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);

            // �õ� value�ֶ��ڌ����еăȴ�ƫ����
            VALUE_OFFSET = UNSAFE.objectFieldOffset(MyCASCounter.class.getDeclaredField("value"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int incrementAndGet() {
        return incrementAndGet(false);
    }

    public int incrementAndGet(boolean debug) {
        for (; ; ) {
            int old = value;
            int next = old + 1;
            if (UNSAFE.compareAndSwapInt(this, VALUE_OFFSET, old, next)) {
                if (debug) {
                    System.out.println(Thread.currentThread().getName()
                            + " | 快照old=" + old + ", 計算next=" + next + " | CAS成功, 寫入" + next);
                }
                return next;
            } else {
                if (debug) {
                    System.out.println(Thread.currentThread().getName()
                            + " | 快照old=" + old + ", 計算next=" + next
                            + " | CAS失敗! 內存已被改為" + value + ", 本次計算作廢, 重試");
                }
            }
        }
    }

    public int get() {
        return value;
    }

}
