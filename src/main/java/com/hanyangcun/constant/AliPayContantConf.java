package com.hanyangcun.constant;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hanyangcun.util.GenerateUtil;

public class AliPayContantConf {


    // 商户appid
    public static String APPID = "2018052860292184";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCOG8tJ1GlCcnqsMR0K0xNXHsFfPL8gkm74Bo64eZA99ZEIY0gtE+fFZDEQKJm3m4KZ8jO9NoR92uLpdNTGuBO9ufth8Q9jEKmnAR3691n6ZZiYp1PAt9vbR7J9/bg4B2SqPIcqD2G3tr5Kf8baQkJ20gN3zuYItfZXAo37TVJdrZrbIPma6noBxmN0cle+xAiwOjTk9FXhDn1oigJU8GS5RPH6dP4drM1I7Edm0//6v4DuLbR490dzB5xSx/JAg+GDDsvjP41paIJBwRPTYY/ExYxbBAHF2A3ySgax8xYSWl/SJL/UZmzt4kt/uGA3WA1l2bbk5elQJjSjUrxsk/yBAgMBAAECggEAK0zav29Cd2WsbUU1xTOVhAkyAJPg5MrZlydgW2NnCQoHHBgsSNf02hdyP6svU1vGCqRbEX9IFZUZaOsZXkd+dGPz7d7mwle0adeUTAEMYJiOS41InsMm8YTCJVOmyyOtj5WL1cwECVdcWhTfAWgJGm1ZcysOun6/RbnYi0b/Ht2uDJe+zRQ2wG+ozaoUxCK/aWQKiXa/YFXiM7v4/RnZ5IQM7cgtlptYt1qwTl+ooOc4Z444JbX23xVdPRsucnUaw7nIMa+cmI741eDyrHDHV8EOmi3qXunFBv3yvYC35MRlrhEKkkyqA8yMdwNnpKMKeNOBSi43WnPuS0YsG+sMAQKBgQDiYyLPh0nLBkTDrEscvmZe5czvLmde7zXBwnUyFVhdv9FoF0M8oybBwK9OXS0m8OcMnNeYqPKj6BZcqq9jkavtPqViD5XosyOowNblltRFjMBIKRWRXdopWHSBXyt86yvCgqRrmwZACnKrgd/q4e6YjuRrBMZadFDGI9rR45gYIQKBgQCgsnrOkhhy7QO+JeF8H6PH6mgYEWAzHsBUdCVKYeOZuAGxRbNsjLqJzIZBavsMdu6Gt5RkMcfxLeSKP9on7GxUGRLXgfOIlUAgee5DWItyVpP4/Ztb+Tnt32uu8ZYbxLwM5s4Sxjl+Z29dfNqKIH7dO8yxwk+CpUgR9iLQHLfYYQKBgQDUe8gIkSqp7FBOeEBn0i2zlG0tcZiEenEyU5o8T9yI4ali0bLCD0ApSvk/zCS9EA9Mv0l8d9aGESgsAehsxCVioBIKYCaMKtelZJpPE6pZPSqckDM2JajGdcekRfdDYFfKUWSRuwe6xBOs9lF83GJdvoCWEf06mUHXN4AC0GPJAQKBgBZ9ZBqM78cFzMRL4ObSNsW/1JmptODyqNApAR8L3CKjUflRW+8RMvoeGZwGMORpsLBB+Q6cuPLUDWwby5hSykZqy+xFT6QJ31OsPOBIABwcadErDRcuJiFYZ2WwgeWY/wh/NNAD1gAO0dMvqhYyvg6QU/F81EvgfdmlrPGbU0+BAoGAaRF0/KnlwdU8FgHQUMza1abc+uvYAGBFz1qxRNCH9UyVDwheME63Y9b9r/WHEYXUGfFVbkkOw35BXFAX/JbVxDx8/LmBHllnEsIr4wW5ML7uUjBd6zZnIj86Q/iAW9aCD2xxU26hJgYzLy0o90pZBWvikKYLp+8ET7IJQFHgQ7Q=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://07132871188.com/hanyangcun/api/pay/aliNotifyOrder";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://07132871188.com/pay_ali_return.html";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArewb/sFb7UujN/rIqYUJ75Z+UJqNU2zualIcMP5vODHrKXJS79oJkMeIi76xWebtOEl+Y8KcfRqfuy/rVs0ICUfM6EygA6UMYBCou7JaOP+2nH++/fBZ6PE+AurB25VooMX7Fpf8+GIy++zHOAon1vT7/tbxlbp7F90oQt4jUjtqp3rw+s0uqQATo27PoAWk8wBIKA0GL9pm9J0BfPO7vy5+7q4UlknLYWROMAG0qjKMXOTajGgRu1aT5g44Oy5zePgJAC8KJtUIl2QpA+m/2lpTZXhI2jnqIxNKzWu2qKyLlJMFoDpRe+IzMoNfrFsUjCVtNWfyQhUfJJMWOFVjDQIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";


    public static void main(String[] avg){

        String outRequestNo = GenerateUtil.generateNo("3");
        String bizContent = "{" +
//                " \"out_trade_no\":\"24000000235358516\"," +
                " \"trade_no\":\"2018081121001004980555422607\"," +
//                " \"out_request_no\":\""+outRequestNo+"\"," +
                " \"refund_amount\":\"0.01\"" +
                " }";//设置业务参数
        try {
//            AlipayClient alipayClient = new DefaultAlipayClient(alConf.getGatewayUrl(), alConf.getAppId(), alConf.getGetMerchantPrivateKey(), "json", "GBK", alConf.getAlipayPublicKey(), alConf.getSignType()); //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, SIGNTYPE); //获得初始化的AlipayClient

            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
            request.setBizContent(bizContent);//设置业务参数
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            String body = response.getBody();
            System.out.println(body); //alipay_trade_refund_response:"data"
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
