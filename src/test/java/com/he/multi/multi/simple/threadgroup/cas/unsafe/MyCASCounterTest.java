package com.he.multi.multi.simple.threadgroup.cas.unsafe;

// ��Ҫʹ��jdk8�h��ȥ�\�д��a
public class MyCASCounterTest {

    public static void main(String[] args) throws InterruptedException {
        MyCASCounter counter = new MyCASCounter();
        // 用2個線程、每個跑5次，方便觀察；改回10/1000可跑完整測試
        Thread[] threads = new Thread[2];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet(true);  // debug=true 打印每一步
                }

            });
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("result: " + counter.get());
    }
}
