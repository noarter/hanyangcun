package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Order", description = "订单对象")
public class Order extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @ApiModelProperty(value = "下单时间", access = "hidden")
    private Long orderTime;

    @ApiModelProperty(value = "入住时间")
    private Long inTime;

    @ApiModelProperty(value = "离开时间")
    private Long outTime;

    @ApiModelProperty(value = "房间数量")
    private Integer roomsNumber;

    @ApiModelProperty(value = "入住天数")
    private Integer nights;

    @ApiModelProperty(value = "入住人数")
    private Integer people;

    @ApiModelProperty(value = "入住成人人数")
    private Integer adultPeople;

    @ApiModelProperty(value = "入住儿童人数")
    private Integer childPeople;

    @ApiModelProperty(value = "联系人姓名")
    private String linkName;

    @ApiModelProperty(value = "联系人电话")
    private String linkPhone;

    @ApiModelProperty(value = "客人姓名")
    private String guests;

    @ApiModelProperty(value = "客人手机号")
    private String guestsPhone;

    @ApiModelProperty(value = "订单总金额")
    private Float orderTotal;

    @ApiModelProperty(value = "折扣价格")
    private Float discountPrice;

    @ApiModelProperty(value = "实付金额")
    private Float actualAmount;

    @ApiModelProperty(value = "订单状态 0-待支付 1-已支付 2-已取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;

    @ApiModelProperty(value = "联系人性别")
    private Integer linkSex;

    @ApiModelProperty(value = "客人性别")
    private Integer guestsSex;

}
