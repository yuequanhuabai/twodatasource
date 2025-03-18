package com.he.multi.leet.palindro;

/**
 *  中心擴展發的 給定字符串，獲取最大回文子串
 */
public class MaxSubPalindro {


    public static void main(String[] args) {
        String s = "babad";
        String longest = longestPalindrome(s);
        System.out.println("最长的回文子串是: " + longest);
    }

    // 返回字符串 s 中最长的回文子串
    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        String longest = "";
        int n = s.length();
        // 遍历每个字符，作为回文中心
        for (int i = 0; i < n; i++) {
            // 奇数长度的回文，以单个字符为中心
            String oddPal = expandAroundCenter(s, i, i);
            if (oddPal.length() > longest.length()) {
                longest = oddPal;
            }
            // 偶数长度的回文，以两个字符之间为中心
            String evenPal = expandAroundCenter(s, i, i + 1);
            if (evenPal.length() > longest.length()) {
                longest = evenPal;
            }
        }
        return longest;
    }

    // 从中心向两边扩展，返回以 left 和 right 为中心的回文子串
    private static String expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // 当 while 循环退出时，s.substring(left+1, right) 为最长回文子串
        return s.substring(left + 1, right);
    }

}
