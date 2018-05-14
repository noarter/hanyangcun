package com.hanyangcun.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private String name;
    private Integer phone;
    private Integer sex;
    private Date createTime;
    private Date updateTime;
}
