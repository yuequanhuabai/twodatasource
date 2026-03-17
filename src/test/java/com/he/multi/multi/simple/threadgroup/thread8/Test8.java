package com.he.multi.multi.simple.threadgroup.thread8;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.LongAdder;

public class Test8 {

    public static void main(String[] args) throws Exception {

        Thread[] threads = new Thread[10];
        int times = 1000;
        Calcutor8 calcutor8 = new Calcutor8();

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Task8(calcutor8, times));
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("calcutor.get:" + calcutor8.getCount());

        // ===== 反射查看 LongAdder 内部结构 =====

        // 1. 反射拿到 Calcutor8 中的 LongAdder count 字段
        Field countField = Calcutor8.class.getDeclaredField("count");
        countField.setAccessible(true);
        LongAdder adder = (LongAdder) countField.get(calcutor8);

        // 2. 反射拿到 Striped64（LongAdder的父类）中的 cells 和 base
        Class<?> striped64 = LongAdder.class.getSuperclass(); // Striped64

        Field baseField = striped64.getDeclaredField("base");
        baseField.setAccessible(true);
        long base = (long) baseField.get(adder);

        Field cellsField = striped64.getDeclaredField("cells");
        cellsField.setAccessible(true);
        Object[] cells = (Object[]) cellsField.get(adder);

        System.out.println("CPU cores: " + Runtime.getRuntime().availableProcessors());
        System.out.println("base: " + base);

        if (cells == null) {
            System.out.println("cells: null (no contention, all increments on base)");
        } else {
            System.out.println("cells.length: " + cells.length);
            // 3. 读取每个 Cell 的 value
            Field valueField = cells[0].getClass().getDeclaredField("value");
            valueField.setAccessible(true);
            for (int i = 0; i < cells.length; i++) {
                long cellValue = (long) valueField.get(cells[i]);
                System.out.println("  cells[" + i + "].value = " + cellValue);
            }
        }
    }
}
