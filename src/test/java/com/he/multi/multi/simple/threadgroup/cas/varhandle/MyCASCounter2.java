package com.he.multi.multi.simple.threadgroup.cas.varhandle;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class MyCASCounter2 {
    private volatile int value;

    private static final VarHandle VALUE;

    static {
        try {
            VALUE = MethodHandles.lookup().findVarHandle(MyCASCounter2.class, "value", int.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public int incrementAndGet() {
        for (; ; ) {
            int old = value;
            int next = old + 1;
            if (VALUE.compareAndSet(this, old, next)) {
                return next;
            }
        }
    }

    public int get() {
        return value;
    }
}
