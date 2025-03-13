package com.he.multi.leet;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TwoPoint {



    @Test
    public void test1(){

        System.out.println("hello world!");


    }


   public static int  MaxSubString(String inputString){

        int left =0,maxLength=0;

       Map<Character,Integer> map = new HashMap<>();
       int length = inputString.length();

       for(int i=0;i<length;i++){
           char ch = inputString.charAt(i);
           if(!map.containsKey(ch)&& i>map.get(left)){
               map.put(ch,i);
               maxLength=i-left+1;

           }
       }

        return 0;
    }











}
