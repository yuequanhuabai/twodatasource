package com.he.multi.multi.simple.threadgroup.thread8;

public class Test8 {

    public static void main(String[] args) throws InterruptedException {

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

    }
}
