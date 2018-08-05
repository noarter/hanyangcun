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
@ApiModel(value = "OrderItems", description = "订单子表对象")
public class OrderItems implements Serializable {
    private static final long serialVersionUID = -3271222153857407417L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "订单主键id")
    private Long orderId;

    @ApiModelProperty(value = "房间id")
    private Long roomsId;

    @ApiModelProperty(value = "房间实际单价")
    private float roomsPrice;

    @ApiModelProperty(value = "预定房间数量")
    private int preRooms;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;

    @ApiModelProperty(value = "房屋类型")
    private String roomType;
}
