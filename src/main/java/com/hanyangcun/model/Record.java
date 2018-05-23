package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "Record", description = "记录对象")
public class Record extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "手机号")
    private Integer phone;

    @ApiModelProperty(value = "优惠券码")
    private Long couponNo;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Date updateTime;
}
