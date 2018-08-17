package com.hanyangcun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/* *
 *类名：AlipayConfig
 *功能：支付宝接口对接基础配置类
 */
//@Data
@Configuration
@PropertySource(value = {"classpath:/config/wxpayconfig.properties"})
public class WXpayConfig  implements Serializable {
    private static final long serialVersionUID = 3477234989070371009L;
    // 微信支付appid
    @Value("${wx.pay.appid}")
    private String appid;
    /*public String getAppID(){
        return this.appid;
    }
    public String getMchID(){
        return this.mch_id;
    }
    public String getKey(){
        return this.key;
    }
    public InputStream getCertStream(){
        return null;
    }
    public IWXPayDomain getWXPayDomain(){
        return null;
    }*/

    // 商户号
    @Value("${wx.pay.mch_id}")
    private String mch_id;

    // 回调通知地址
    @Value("${wx.pay.notify_url}")
    private String notify_url;


    // no_credit--指定不能使用信用卡支付
    @Value("${wx.pay.limit_pay}")
    private String limit_pay;

    // sign_type 加签类型
    @Value("${wx.pay.sign_type}")
    private String sign_type;

    // key为商户平台设置的密钥key
    @Value("${wx.pay.key}")
    private String key;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getLimit_pay() {
        return limit_pay;
    }

    public void setLimit_pay(String limit_pay) {
        this.limit_pay = limit_pay;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}