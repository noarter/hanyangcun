package com.hanyangcun.wxpay;

import lombok.Data;

import java.io.Serializable;

@Data
public class WXPayModel  implements Serializable {
    private static final long serialVersionUID = 3477800389170371046L;

    private String 	body;//商品描述
    private String out_trade_no;//商户订单号
    private Integer total_fee;//总金额
    private String spbill_create_ip;//终端IP
    private String trade_type;//交易类型
    private String scene_info;//场景信息
    private String nonce_str;//随机字符串

    /** 公众号支付 必传*/
    private String openid;//用户标识

}
