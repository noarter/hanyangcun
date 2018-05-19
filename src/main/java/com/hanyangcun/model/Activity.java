package com.hanyangcun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    private Long id;
    private String name;
    private String content;
    private Date startTime;
    private Date endTime;
    private float discountPrice;
    private Integer useCount;
    //状态：1-上架2-下架
    private Integer status;
    private Date createTime;
    private Date updateTime;
    //活动状态：1-未开始2-已开始3-已结束
    private Integer activityStatus;
}
