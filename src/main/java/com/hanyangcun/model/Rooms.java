package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Rooms", description = "客房对象")
public class Rooms extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "客房类型")
    private String roomType;

    @ApiModelProperty(value = "客房数量")
    private Integer roomNumber;

    @ApiModelProperty(value = "当前已定客房数量")
    private Integer usedRoom;

    @ApiModelProperty(value = "当前剩余客房数量")
    private Integer remainedRoom;

    @ApiModelProperty(value = "客房价格")
    private float roomPrice;

    @ApiModelProperty(value = "客房折扣价")
    private float discountPrice;

    @ApiModelProperty(value = "客房折扣比列")
    private Integer discountRate;

    @ApiModelProperty(value = "周期")
    private String weekDate;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", access = "hidden")
    private Date updateTime;
}
