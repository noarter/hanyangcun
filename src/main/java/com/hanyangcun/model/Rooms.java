package com.hanyangcun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rooms {
    private Long id;
    private Integer type;
    private Integer roomNumber;
    private Integer usedRoom;
    private Integer remainedRoom;
    private float roomPrice;
    private float discountPrice;
    private Integer discountRate;
    private String weekDate;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
