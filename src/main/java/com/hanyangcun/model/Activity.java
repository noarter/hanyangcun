package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Activity", description = "活动对象")
public class Activity extends PageEntity {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "活动主题")
    private String name;

    @ApiModelProperty(value = "短信模板code")
    private String templateCode;

    @ApiModelProperty(value = "活动内容", access = "hidden")
    private String content;

    @ApiModelProperty(value = "活动开始时间")
    private Long startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Long endTime;

    @ApiModelProperty(value = "优惠券价格")
    private float discountPrice;

    @ApiModelProperty(value = "优惠券使用数量")
    private Integer useCount;

    @ApiModelProperty(value = "上下架状态：0-上架1-下架")
    private Integer status;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;

    @ApiModelProperty(value = "活动状态：1-未开始2-已开始3-已结束")
    private Integer activityStatus;

}
