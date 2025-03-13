package com.he.multi.leet;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.he.multi.leet.LongestSubstringRecursive.rec;

public class Fibonacci {

    /**
     * 斐波拉契數組；
     */
    @Test
    public void test1() {

        int x = 5;
        int fabonacci = fabonacci1(x);
        System.out.println("The "+x+" fabonacci is : " + fabonacci);
    }


    /**
     * 返回地n個斐波那契數的值
     *
     * @param n
     * @return
     */
    public static int fabonacci(int n) {

        int preNum = 0;
        int prePreNum = 1;

        int value = 0;
        if (0 == n) {
            return 0;
        }
        if (1 == n) {
            return 1;
        }

        for (int i = 2; i < n; i++) {
            value = fabonacci(i - 2) + fabonacci(i - 1);
            preNum = prePreNum;
            prePreNum = value;
        }


        return value;
    }


    /**
     * 返回地n個斐波那契數的值
     *
     * @param n
     * @return
     */
    public static int fabonacci1(int n) {

        if (1 == n) {
            return 1;
        }
        if (2 == n) {
            return 1;
        }

        return  fabonacci1(n - 1) + fabonacci1(n - 2);
    }


    public static String s ="";

    /**
     * 字符串的最長字串的遞歸解決方案：
     */
    @Test
    public void test2() {
        String s = "asdf";


    }

    /**
     * 要獲取某個字符串的最長不重複子字符串的長度和它的 length-1 的最長不重複子字符串有關
     * length-1的最長不重複子字符串的長度又和length-2的子字符串有關
     * ...
     * 最後和該字符串的前兩個字符有關，和首字母有關；
     * @param inputstring
     * @return
     */
    public static int maxLengthOfSubstring(String inputstring) {
        inputstring = inputstring;
        int maxLength = 0;
        if (inputstring.length() == 0) return 0;
        if (inputstring.length() == 1) return 1;

        char c = inputstring.charAt(0);
        for(int i=2; i<inputstring.length(); i++){
            maxLength= Math.max(maxLength, reci(i-1));
        }


        return maxLength;
    }

    private static int reci(int i) {
        // 當前字符

        char c = s.charAt(i);

        //


        return 0;
    }



    public static int maxSubstring(String inputstring){

        // 用來存儲字符及它的最新位置
        Map<Character,Integer> map = new HashMap<>();
        // 默認字符串的最大字串的長度為0
        int maxlength=0;
        // 指針的默認位置在首字符的位置
        int left =0;

        for(int i=0;i<inputstring.length();i++){
            char c = inputstring.charAt(i);

            if(map.containsKey(c) && map.get(c)>=left){
               left=map.get(c)+1;
            }
             map.put(c,i);
            // 當前字符的最長非重複字串的長度和之前的最長不重複字串比較
          maxlength=  Math.max(maxlength,i-left+1);
        }

        return maxlength;
    }
}
