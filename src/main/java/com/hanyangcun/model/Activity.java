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
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
