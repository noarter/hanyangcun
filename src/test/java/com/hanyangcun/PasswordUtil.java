package com.hanyangcun;

import com.hanyangcun.util.GenerateUtil;
import com.hanyangcun.util.SHA512Util;

public class PasswordUtil {
    public static void main(String[] args) {
        String salt = GenerateUtil.generateSalt();
        System.out.println(salt);
        String encPassword = SHA512Util.encry512("admin" + "admin" + salt);
        System.out.println(encPassword);
    }
}
