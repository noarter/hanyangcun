package com.hanyangcun.util;

public class StringUtils {

    /**判断非空且去空格后不为空字符串*/
    public static boolean isNotNullNorEmptyAfterTrim(String value){
        return value != null && !value.trim().isEmpty();
    }
    /**判断空或去空格后为空字符串*/
    public static boolean isNullOrEmptyAfterTrim(String value){
        return value == null || value.trim().isEmpty();
    }

}
