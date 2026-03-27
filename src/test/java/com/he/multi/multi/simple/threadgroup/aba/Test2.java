package com.he.multi.multi.simple.threadgroup.aba;

public class Test2 {
    public static void main(String[] args) {
        int[] arr={0};
        change(arr);
        System.out.println(arr[0]);
    }

    private static void change(int[] arr) {
        arr = new int[]{999};
    }
}
