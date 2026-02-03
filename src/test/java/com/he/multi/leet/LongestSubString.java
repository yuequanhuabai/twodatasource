package com.he.multi.leet;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LongestSubString {

    /**
     * 最長不重複子串的解決思路：
     * 1. 循環遍歷，應破解法
     * 2. 滑動窗口 （ 分使用map和不使用map的ASCII字符）
     * 3. 動態規劃
     * 4. 遞歸
     * 5.雙指針+Set
     * 6
     */

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
            if (i - currentCharPosition > arr[i - 1]) {
                arr[i] = arr[i - 1] + 1;
            } else {
                arr[i] = i - currentCharPosition;
            }


        }


        return 0;
    }


    @Test
    public void test2() {
//        String s = "asdfsadflks";

        String s = "asrdqrwers";
        int i = lengthOfLongestSubstring(s);
        System.out.println("i = " + i);
    }

    /**
     * 暴力法求解最长无重复子串的长度
     * 时间复杂度：O(n^2)
     */
    public static int lengthOfLongestSubstring(String s) {
        int maxLength = 0;
        // 从每个字符作为起点开始扫描
        for (int i = 0; i < s.length(); i++) {
            // 使用一个Set来记录当前子串中的字符
            java.util.HashSet<Character> set = new java.util.HashSet<>();
            int currentLength = 0;
            for (int j = i; j < s.length(); j++) {
                char c = s.charAt(j);
                // 如果当前字符已出现，则停止扩展
                if (set.contains(c)) {
                    break;
                } else {
                    set.add(c);
                    currentLength++;
                }
            }
            // 更新最大长度
            maxLength = Math.max(maxLength, currentLength);
        }
        return maxLength;
    }


    public static int DoubleFor(String str) {

        int length = str.length();

        int maxLength = 0;

        for (int i = 0; i < length; i++) {

            int currentLength = 0;
            HashSet<Character> set = new HashSet<>();
            for (int j = i; j < length; j++) {
                char c = str.charAt(j);
                if (set.contains(c)) {
                    break;
                } else {
                    set.add(c);
                    currentLength++;
                }
            }
            maxLength = Math.max(maxLength, currentLength);

        }
        return maxLength;
    }


    @Test
    public void test3() {
        String s = "asdfsdf";
        int maxsubstring = maxsubstring(s);
        System.out.println("maxsubstring = " + maxsubstring);
//        System.out.println("asdfasdf");
    }

    /**
     * 思路，從子第一個字符開始遍歷，記住每個字符及以它為起點的最大不重複字串的長度為臨時最長的字串長度tempLength
     * 當和下一個字符進行比較的時候，較大的賦值給全局變量的最大字符長度maxLength
     * 直至依次遍歷完整個字符串
     *
     * (記住每個字符及以它為起點的最大不重複字串的長度為臨時最長的字串長度tempLength)
     * 這裏有問題：無論是某個字符開始的子字符串裏面會出現其它字符的重複造成整個子字符串的重複，所以要確定一個字符串是非重複的就要確定他的子字符串是非重複的；
     * 這是一個遞歸的邏輯；核心就是判斷該字符產和去掉最後一個字符后的子字符串時候為非重複子字符串；
     * 核心就是判斷一個子字符串裏是否有重複的字符，原字符串的最大不重複子字符串的長度和子字符串的最大不重複字串和後面追加的一個字符是否為子字符串
     * 裏面的字符相關；大問題分解爲了小問題和後綴字符關係的問題；小問題又可以分解為它的子字符串和最後一個字符的關係；形成遞歸邏輯；
     *
     */

    public static int maxsubstring(String inputString) {
        int length = inputString.length();

        if (length == 0) return 0;
        if (length == 1) return 1;

//        Map<Character,Integer> map = new HashMap<>();
//        Map<Character, Integer> map2 = new HashMap<>();

        int maxLength = 1;
        for (int i = 0; i < length; i++) {
            int currentLength = 1;
            char c = inputString.charAt(i);
//            map2.put(c, i);
            for (int j = i + 1; j < length; j++) {
                char c1 = inputString.charAt(j);
                if (c == c1) {
                    break;
                }
                currentLength++;
            }
            maxLength = Math.max(currentLength, maxLength);
        }

        return maxLength;
    }
}
