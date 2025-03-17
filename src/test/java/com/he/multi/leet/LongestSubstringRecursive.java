package com.he.multi.leet;

import java.util.HashMap;

public class LongestSubstringRecursive {



    // 用来记录每个字符上一次出现的位置（全局维护）
    private static HashMap<Character, Integer> lastIndex = new HashMap<>();
    // memo[i] 缓存以 s[i] 结尾的最长无重复子串的长度
    private static int[] memo;
    private static String s;

    public static int rec(int i) {
        // 基本情况
        if (i == 0) {
            lastIndex.put(s.charAt(0), 0);
            memo[0] = 1;
            return 1;
        }
        // 如果已计算，直接返回
        if (memo[i] != -1) {
            return memo[i];
        }

        char c = s.charAt(i);
        int res;
        if (!lastIndex.containsKey(c)) {
            // 没有出现过，直接延伸前一状态
            res = rec(i - 1) + 1;
        } else {
            int prevIndex = lastIndex.get(c);
            int prevLen = rec(i - 1);
            // 如果当前字符上次出现的位置在当前子串之外，则可延伸
            if (i - prevIndex > prevLen) {
                res = prevLen + 1;
            } else {
                // 否则，最长子串只能从 prevIndex + 1 开始
                res = i - prevIndex;
            }
        }
        // 更新当前字符的最后出现位置
        lastIndex.put(c, i);
        memo[i] = res;
        return res;
    }

    public static int lengthOfLongestSubstring(String input) {
        s = input;
        int n = s.length();
        if (n == 0) return 0;
        memo = new int[n];
        // 初始化 memo 数组为 -1，表示未计算
        for (int i = 0; i < n; i++) {
            memo[i] = -1;
        }
        lastIndex.clear();

        int maxLen = 0;
        // 计算每个位置的最长无重复子串
        for (int i = 0; i < n; i++) {
            maxLen = Math.max(maxLen, rec(i));
        }
        return maxLen;
    }

    public static void main(String[] args) {
        String s = "abcabcbb";
        System.out.println("输入: " + s);
        System.out.println("输出: " + lengthOfLongestSubstring(s)); // 输出应为 3
    }



    public static int lengthOfLongestSubstringDP(String s) {
        int n = s.length();
        if (n == 0) return 0;

        int[] dp = new int[n]; // dp[i] 表示以 s[i] 结尾的最长无重复子串长度
        dp[0] = 1; // 第一个字符的子串长度为 1
        HashMap<Character, Integer> lastIndex = new HashMap<>();
        lastIndex.put(s.charAt(0), 0);
        int maxLen = 1;

        for (int i = 1; i < n; i++) {
            char c = s.charAt(i);
            if (!lastIndex.containsKey(c) || i - lastIndex.get(c) > dp[i - 1]) {
                dp[i] = dp[i - 1] + 1;
            } else {
                dp[i] = i - lastIndex.get(c);
            }
            lastIndex.put(c, i);
            maxLen = Math.max(maxLen, dp[i]);
        }

        return maxLen;
    }

}
