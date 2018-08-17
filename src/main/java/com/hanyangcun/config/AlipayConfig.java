package com.hanyangcun.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/* *
 *类名：AlipayConfig
 *功能：支付宝接口对接基础配置类
 */
@Data
@Configuration
@PropertySource(value = {"classpath:/config/alipayconfig.properties"})
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    @Value("${ali.pay.app_id}")
    private String appId;

    // 商户私钥，您的PKCS8格式RSA2私钥
    @Value("${ali.pay.merchant_private_key}")
    private String getMerchantPrivateKey;

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    @Value("${ali.pay.alipay_public_key}")
    private String alipayPublicKey;

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${ali.pay.notify_url}")
    private String notifyUrl;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${ali.pay.return_url}")
    private String returnUrl;

    // 签名方式
    @Value("${ali.pay.sign_type}")
    private String signType;

    // 字符编码格式
    @Value("${ali.pay.charset}")
    private String charset;

    // 支付宝网关
    @Value("${ali.pay.gatewayUrl}")
    private String gatewayUrl;

    // 授权访问令牌的授权类型
    @Value("${ali.pay.grant_type}")
    private String grant_type;

}