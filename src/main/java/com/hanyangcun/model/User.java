package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "User", description = "用户对象")
public class User extends PageEntity {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "盐值", access = "hidden")
    private String salt;

    @ApiModelProperty(value = "禁用", access = "hidden")
    private Integer locked;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "修改时间", access = "hidden")
    private Long updateTime;

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;
}
