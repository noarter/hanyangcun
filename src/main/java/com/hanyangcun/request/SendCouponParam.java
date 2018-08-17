package com.hanyangcun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SendCouponParam", description = "批量发送优惠券信息")
public class SendCouponParam {

    @ApiModelProperty(value = "发送手机号集合")
    private String[] phones;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "短信模板code")
    private String templateCode;
}
