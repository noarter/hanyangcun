package com.hanyangcun.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class GenerateUtil {
    /**
     * 密码盐值
     * @return
     */
    public static String generateSalt(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 订单号15位
     * @return
     */
    public static String generateOrderNo() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0; 15 代表长度为15; d 代表参数为正数型
        return String.valueOf((int) (Math.random() * 9 + 1)) + String.format("%015d", hashCodeV);
    }

    /**
     * 优惠券码13位
     * @return
     */
    public static String generateCouponNo(){
        return String.valueOf(System.nanoTime()).substring(5)+RandomStringUtils.random(4,false,true);
    }
}
