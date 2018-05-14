package com.hanyangcun.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class HttpToken {
    public static String getToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("access_token");
            return JWTUtil.isTokenExpired(token) ? null : token;
        } catch (Exception e) {
            log.error("获取token异常:{}", e.getMessage(), e);
            return null;
        }
    }
}
