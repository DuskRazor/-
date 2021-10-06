package com.kali;

/**
 * 测试类
 * 统计出两个数组中相同元素的个数
 */
public class T {
    public static void main(String[] args) {
        int[] red = {30, 1, 9, 20, 11, 15, 41};
        int[] blue = {15, 7, 1, 30, 22, 13, 40};

        int count = 0;
        boolean find;

        for(int i=0; i<red.length; i++){
            find = false;
            for(int j = 0; j < blue.length; j++){
                if(red[i] == blue[j]) {
                    if(!find) {
                        count ++;
                        find = true;
                    }
                    blue[j] = -1;
                }
            }
        }
        System.out.println("数组red与数组blue有" + count + "个相同元素");
    }
}