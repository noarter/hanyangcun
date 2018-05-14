package com.hanyangcun.util;

import java.util.UUID;

public class GenerateUtil {
    public static String generateSalt(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
