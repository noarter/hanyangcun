package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PayRecord", description = "支付记录表")
public class PayRecord  implements Serializable {
    private static final long serialVersionUID = 3477839989070371009L;

    @ApiModelProperty(value = "商户订单号")
    private String orderNo;

    @ApiModelProperty(value = "支付类型：支付类型：1-电脑网站支付(支付宝)2-手机网页支付(支付宝)3-扫码支付(微信)4-公众号支付(微信)5-H5支付(微信)")
    private Integer type;

    @ApiModelProperty(value = "openid")
    private String openid;

    @ApiModelProperty(value = "id", access = "hidden")
    private Long id;

    @ApiModelProperty(value = "支付单号", access = "hidden")
    private String payNo;

    @ApiModelProperty(value = "交易流水号", access = "hidden")
    private String tradeNo;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;

    @ApiModelProperty(value = "支付状态", access = "hidden")
    private Integer payState;

    @ApiModelProperty(value = "请求ip", access = "hidden")
    private String spbillCreateIp;

    @ApiModelProperty(value = "退款流水号", access = "hidden")
    private String outRequestNo;

}
