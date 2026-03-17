package com.he.multi.multi.simple.threadgroup.add.thread7;

public class Test7 {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];

        Calcutor calcutor = new Calcutor();
        int times = 1000;
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Task7(times, calcutor));

        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("calcuter.getA():" + calcutor.getA());

    }
}
