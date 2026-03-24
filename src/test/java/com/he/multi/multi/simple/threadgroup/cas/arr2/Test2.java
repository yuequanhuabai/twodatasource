package com.he.multi.multi.simple.threadgroup.cas.arr2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Test2 {

    private final AtomicIntegerArray  bucket = new AtomicIntegerArray(10);


    public void record(int value){
        if(value<1 || value>100) return;
        int index = (value - 1) / 10;
        bucket.incrementAndGet(index);
    }

    public void printDistribution(){
        for(int i=0;i<bucket.length();i++){
            int start = i*10+1;
            int end=(i+1)*10;
            System.out.printf("[%3d-%3d]: %d%n",start,end,bucket.get(i));
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Test2 test2 = new Test2();
        Random random = new Random();
        Thread[] threads = new Thread[10];

        for(int i=0;i<10;i++){
            threads[i] =new Thread(()->{
                for(int j=0;j<10000;j++){
                    int value=random.nextInt(100)+1;
                    test2.record(value);
                }
            });
            threads[i].start();
        }

        for(int i=0;i<10;i++){
            threads[i].join();
        }
        test2.printDistribution();
    }
}
