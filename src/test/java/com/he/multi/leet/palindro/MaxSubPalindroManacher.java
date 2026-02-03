package com.he.multi.leet.palindro;

/**
 *  Manacher 算法實現給定字符串的最大回文子串；
 */
public class MaxSubPalindroManacher {
    public static void main(String[] args) {
        String s = "babad";
        String longest = longestPalindrome(s);
        System.out.println("最长回文子串是: " + longest);
    }

    // 返回给定字符串 s 的最长回文子串
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        // 预处理字符串，在字符间加上特殊符号
        char[] processed = preprocess(s);
        int n = processed.length;
        int[] p = new int[n];  // p[i] 表示以 processed[i] 为中心的回文半径
        int center = 0, right = 0;
        int maxLen = 0, centerIndex = 0;

        // 遍历处理后的字符串
        for (int i = 0; i < n; i++) {
            int mirror = 2 * center - i;  // i 关于 center 的对称点
            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            } else {
                p[i] = 0; // i 不在当前回文范围内，从0开始扩展
            }

            // 尝试向左右扩展
            while (i + p[i] + 1 < n && i - p[i] - 1 >= 0 && processed[i + p[i] + 1] == processed[i - p[i] - 1]) {
                p[i]++;
            }

            // 如果当前回文串扩展到了新的边界，更新 center 和 right
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }

            // 记录最大回文半径及中心位置
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }

        // 从 processed 数组回推到原始字符串
        // 在预处理过程中，每两个字符之间都插入了 '#'，因此原始字符串的起始索引为 (centerIndex - maxLen) / 2
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }

    // 预处理函数，将原始字符串转换为带特殊符号的字符数组
    private static char[] preprocess(String s) {
        // 新数组长度为原字符串长度*2 + 1
        char[] processed = new char[s.length() * 2 + 1];
        for (int i = 0; i < processed.length; i++) {
            // 每个偶数位置放入 '#'，奇数位置放入原字符串的字符
            processed[i] = (i % 2 == 0) ? '#' : s.charAt(i / 2);
        }
        return processed;
    }
}
