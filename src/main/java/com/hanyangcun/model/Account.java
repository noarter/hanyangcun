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
@ApiModel(value = "Account", description = "帐户对象")
public class Account extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private Integer phone;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Date updateTime;
}
