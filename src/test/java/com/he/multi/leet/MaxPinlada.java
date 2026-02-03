package com.he.multi.leet;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaxPinlada {

    public static void main(String[] args) {

//        System.out.println("hello world!");

        int a = 12343211;

        boolean aaa = judge(a);

        System.out.println("aaa= " + aaa);

    }

    private static boolean judge(int a) {

        int revertNumber = 0;
        while (a > revertNumber) {
            revertNumber = 10 * revertNumber + a % 10;
            a = a / 10;

            System.out.println("a :" + a + " revertNumber:" + revertNumber);

        }


        return a == revertNumber || revertNumber / 10 == a;
    }


    @Test
    public void test1() {
        String s = "asdfdsa";

        boolean a = palindrome(s);


        System.out.println("a:" + a);

    }

    private boolean palindrome(String s) {

        int right = s.length() - 1;
        int left = 0;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }


    @Test
    public void test2() {
        String s = "adsda";

        String substring = s.substring(5, 6);
        System.out.println("substring:" + substring);

//        boolean bo = palindromeNumber(s);


//        Set<String> palindromicSubstrings1 = findPalindromicSubstrings1(s);
//        System.out.println("palindromicSubstrings1: " + palindromicSubstrings1);

//        List<String> palindromicSubstrings = findPalindromicSubstrings(s);
//        System.out.println("palindromicSubstrings: " + palindromicSubstrings);

    }

    private boolean palindromeNumber(String s) {



        return false;
    }



//    // 返回字符串 s 中所有的回文子串，使用 List 存储
    public static List<String> findPalindromicSubstrings(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() == 0) {
            return result;
        }
        int n = s.length();
        // 遍历每个字符，作为回文中心
        for (int i = 0; i < n; i++) {
            // 奇数长度的回文：中心为一个字符
            expandAroundCenter(s, i, i, result);
            // 偶数长度的回文：中心为两个字符之间的位置
            expandAroundCenter(s, i, i + 1, result);
        }
        return result;
    }


    // 从中心开始向两边扩展，寻找回文子串
    private static void expandAroundCenter(String s, int left, int right, List<String> result) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            // 找到回文子串，将其加入结果集中
            result.add(s.substring(left, right + 1));
            left--;
            right++;
        }
    }



//    // 返回字符串 s 中所有的回文子串，使用 Set 去重
    public static Set<String> findPalindromicSubstrings1(String s) {
        Set<String> result = new HashSet<>();
        if (s == null || s.length() == 0) {
            return result;
        }
        int n = s.length();
        // 遍历每个字符，作为回文中心
        for (int i = 0; i < n; i++) {
            // 奇数长度的回文：中心为一个字符
            expandAroundCenter1(s, i, i, result);
            // 偶数长度的回文：中心为两个字符之间的位置
            expandAroundCenter1(s, i, i + 1, result);
        }
        return result;
    }

    // 从中心开始向两边扩展，寻找回文子串
    private static void expandAroundCenter1(String s, int left, int right, Set<String> result) {
        int tempLength = s.length();
//        char leftChar = ;
//        char rightChar = ;
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            // 找到回文子串，将其加入结果集中
            result.add(s.substring(left, right + 1));
            left--;
            right++;
        }
    }




    @Test
    public void test3(){

        System.out.println("hello");
        String s = "sdfds";

       List<String > list = getPindromeNumber(s);

        System.out.println("list:" + list);
    }

    private List<String> getPindromeNumber(String s) {
        List<String> result = new ArrayList<>();
        if(s == null || s.length() == 0){
            return result;
        }

        for (int i = 0; i < s.length(); i++) {
            getOddNumber(s,i,i,result);
            getEvenNumber(s,i,i+1,result);
        }

        return result;
    }

    private void getEvenNumber(String s, int left, int right, List<String> result) {
        while (left >=0 && right < s.length()&& s.charAt(left)==s.charAt(right)){
            result.add(s.substring(left, right + 1));
            left--;
            right++;
        }
    }

    private void getOddNumber(String s, int left, int right, List<String> result) {

        while (left >=0 && right < s.length()&& s.charAt(left)==s.charAt(right)){
            result.add(s.substring(left, right + 1));
            left--;
            right++;
        }
    }




    @Test
    public void test4(){
        String s ="qwewq";
        String s1 = longestPalindrome(s);
        System.out.println("s:" + s1);
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



    @Test
    public void test5(){
        String s ="qwewq";

    }

}
