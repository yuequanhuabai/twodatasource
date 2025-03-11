package com.he.multi.leet;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test1 {

    public static void main(String[] args) {
        String ss = "fdweghaasdaf";

        int i = maxLength(ss);
        System.out.println("i = " + i);
//        Map<String, Object> map = maxLengthSubstring(ss);
//        System.out.println(map);
    }

    @Test
    public void test1() {
        String s = "sadfsdf";
        maxLengthSubstring(s);
//        String s = "hello world!";

//        String s = "heo wollrld!";
//        System.out.println(s);
//        int i = lengthOfLongestSubstring(s);
//        System.out.println("lengthOfLongestSubstring: " + i);
//
//        String s1 = longestSubstring(s);
//        System.out.println("longestSubstring: " + s1);
//        String substring = s.substring(1, 2);
//        System.out.println("substring: " + substring);
//        String substring1 = s.substring(1);
//        System.out.println("substring1: " + substring1);
    }


    public static int lengthOfLongestSubstring(String s) {
        // 使用 HashMap 存储每个字符最后出现的索引位置
        HashMap<Character, Integer> charIndex = new HashMap<>();
        // 滑动窗口左边界
        int left = 0;
        // 保存当前最长不重复子串的长度
        int maxLength = 0;

        // 遍历字符串，right 表示窗口右边界
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            // 如果当前字符已经出现过，并且它上一次出现的位置不在当前窗口左边界之前，
            // 则移动左边界到当前字符上一次出现位置的右侧一位
            if (charIndex.containsKey(currentChar) && charIndex.get(currentChar) >= left) {
                left = charIndex.get(currentChar) + 1;
            }
            // 更新当前字符的最新出现位置
            charIndex.put(currentChar, right);
            // 计算当前窗口的长度并更新最大长度
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }


    public static String longestSubstring(String s) {
        // 使用 HashMap 存储每个字符最后出现的索引位置
        HashMap<Character, Integer> charIndex = new HashMap<>();
        // 滑动窗口左边界
        int left = 0;
        // 保存当前最长不重复子串的长度
        int maxLength = 0;
        String maxSubstring = "";
        int right1 = 0;
        // 遍历字符串，right 表示窗口右边界
        for (int right = 0; right < s.length(); right++) {
            char currentChar = s.charAt(right);
            // 如果当前字符已经出现过，并且它上一次出现的位置不在当前窗口左边界之前，
            // 则移动左边界到当前字符上一次出现位置的右侧一位
            if (charIndex.containsKey(currentChar) && charIndex.get(currentChar) >= left) {
                left = charIndex.get(currentChar) + 1;
            }
            // 更新当前字符的最新出现位置
            charIndex.put(currentChar, right);
            // 计算当前窗口的长度并更新最大长度
            maxLength = Math.max(maxLength, right - left + 1);
            maxSubstring = s.substring(left, right + 1).length() > maxSubstring.length() ? s.substring(left, right + 1) : maxSubstring;
            right1 = right1 >= right ? right1 : right;
        }
        String s1 = s.substring(left, right1);
        System.out.println("maxSubstring:= " + maxSubstring);

        return s1;
    }

    /**
     * 遍歷一次，對每個字符，存入map中，每個子字符串的初始left為0，right為當前字符的下標；
     * 最小子字符串為s.substring(left,right)最小子字符串的長度為（right-left+1）
     * 儅字符串中出現重複的時候，把left置爲重複字符串字符的下標+1
     *
     * @param inputString
     * @return
     */
    public static Map<String, Object> maxLengthSubstring(String inputString) {
        Map<String, Object> map = new HashMap<>();


//        String substring = inputString.substring(6, 7);
//        System.out.println("substring:= " + substring);

        Map<Character, Integer> storeMap = new HashMap<>();
        String maxSubString = "";
        int maxLength = 0;
        int left = 0;

//        int right = 0;

        for (int right = 0; right < inputString.length(); right++) {
            char currentChar = inputString.charAt(right);
//            && storeMap.get(currentChar) >= left
            if ( storeMap.containsKey(currentChar) && storeMap.get(currentChar) >= left) {
                left = storeMap.get(currentChar) + 1;
            }
            storeMap.put(currentChar, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        map.put("maxLength", maxLength);
//        map.put("maxSubstring","");
        return map;
    }


    public static int maxLength(String inputString){
        Map<Character, Integer> storeMap = new HashMap<>();

        String maxSubString = "";
        int maxLength = 0;
        for(int i = 0; i < inputString.length(); i++){
            Set<Character> set = new HashSet<>();
            for(int j = i; j < inputString.length(); j++){
                if(set.contains(inputString.charAt(j))){
                    break;
                }else {
                    set.add(inputString.charAt(j));
                }
            }
           maxLength= Math.max(maxLength,set.size());
        }
        return maxLength;
    }


    @Test
    public void test2(){

        String s = "abcabcbb";
        System.out.println("动态规划方法 - 输入: " + s);
        System.out.println("输出: " + lengthOfLongestSubstringDP(s));  //
    }



    /**
     * 使用动态规划求解最长不重复子串的长度
     * dp[i] 表示以 s[i] 结尾的不重复子串的长度
     */
    public static int lengthOfLongestSubstringDP(String s) {
        int n = s.length();
        if (n == 0) return 0;

        int[] dp = new int[n];            // dp[i] 表示以 s[i] 结尾的最长无重复子串长度
        dp[0] = 1;                        // 第一个字符的子串长度为1
        HashMap<Character, Integer> lastIndex = new HashMap<>();
        lastIndex.put(s.charAt(0), 0);
        int maxLen = 1;

        for (int i = 1; i < n; i++) {
            char c = s.charAt(i);
            // 如果字符 c 没出现过，则 dp[i] = dp[i-1] + 1
            if (!lastIndex.containsKey(c)) {
                dp[i] = dp[i - 1] + 1;
            } else {
                int prevIndex = lastIndex.get(c);
                // 如果当前字符上次出现距离 i 超过了 dp[i-1]，说明当前子串没有包含重复的 c
                if (i - prevIndex > dp[i - 1]) {
                    dp[i] = dp[i - 1] + 1;
                } else {
                    // 否则，最长子串只能从 prevIndex + 1 开始
                    dp[i] = i - prevIndex;
                }
            }
            // 更新当前字符的最后出现位置
            lastIndex.put(c, i);
            maxLen = Math.max(maxLen, dp[i]);
        }

        return maxLen;
    }
}
