package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CouponRecord", description = "记录对象")
public class CouponRecord extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "优惠券码")
    private String couponNo;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;
}
