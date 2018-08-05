package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PayRecord", description = "支付记录表")
public class PayRecord {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商户订单号")
    private String orderNo;

    @ApiModelProperty(value = "支付类型")
    private Integer type;

    @ApiModelProperty(value = "交易流水号")
    private String tradeNo;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;
}
