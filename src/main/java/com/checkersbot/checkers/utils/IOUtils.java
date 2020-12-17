package com.checkersbot.checkers.utils;

import java.util.Arrays;

public class IOUtils {
    public static String getTwoDimensionalArrayInString(int[][] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] ints : arr){
            stringBuilder.append(Arrays.toString(ints));
        }
        return stringBuilder.toString();
    }
}
