package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "Coupon", description = "优惠券对象")
public class Coupon extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "优惠券码")
    private Long couponNo;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Date updateTime;
}
