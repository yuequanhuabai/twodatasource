package com.he.multi.leet.palindro;

/**
 *  动态规划（Dynamic Programming） 獲取給定字符串的最大回文子串；
 */
public class MaxSubPalindroDP {

    public static void main(String[] args) {
        String s = "babad";
        System.out.println("最长回文子串为: " + longestPalindrome(s));
    }



    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        String longest = "";

        // 从右下角向左上角填表
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                // s.charAt(i) 与 s.charAt(j) 相等，并且内部子串为回文（或者长度不足3）
                if (s.charAt(i) == s.charAt(j) && (j - i < 3 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    if (j - i + 1 > longest.length()) {
                        longest = s.substring(i, j + 1);
                    }
                }
            }
        }
        return longest;
    }


}
