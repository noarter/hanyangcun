package com.hanyangcun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String orderNo;
    private Integer orderType;
    private Date inTime;
    private Date outTime;
    private Integer roomsNumber;
    private Integer nights;
    private Integer people;
    private String linkName;
    private Integer linkPhone;
    private String guests;
    private Integer guestsPhone;
    private float orderTotal;
    private float discountPrice;
    private float actualAmount;
    private Integer orderStatus;
    private Date orderTime;
    private Date updateTime;
}
