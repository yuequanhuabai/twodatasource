package com.he.multi.leet;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class LongestSubString {

    private static int[] arr;
    private static Map<Character, Integer> map = new HashMap<Character, Integer>();
    private static String s;


    @Test
    public void test1() {
//        System.out.println("hello java!");
        String s = "asdfsadflks";
        getMaxLengthOfString(s);
    }


    // 使用遞歸的方式完成
    public static int getMaxLengthOfString(String input) {
        s = input;
        int length = s.length();
        if (length == 0) {
            return 0;
        }

        for (int i = 1; i < length; i++) {
            arr[i] = -1;
        }

        int maxLength = 0;

        for (int i = 1; i < length; i++) {
            maxLength = Math.max(maxLength, recisive(i));
        }

        return maxLength;
    }

    // 返回以當前字符結尾的最大不重複字串的長度；
    private static int recisive(int i) {

        if (0 == i) {
            map.put(s.charAt(0), 0);
            arr[0] = 1;
            return 1;
        }

        if (arr[i] != -1) {
            return arr[i];
        }

        char currentChar = s.charAt(i);

        if (!map.containsKey(currentChar)) {
            map.put(currentChar, arr[i - 1] + 1);

        } else {

            int currentCharPosition = map.get(currentChar);
//            preLength
            if(i-currentCharPosition > arr[i-1]) {
                arr[i] = arr[i-1] + 1;
            }else {
                arr[i] = i-currentCharPosition;
            }






        }


        return 0;
    }
}
