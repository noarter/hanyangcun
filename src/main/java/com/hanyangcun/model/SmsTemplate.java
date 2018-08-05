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
@ApiModel(value = "SmsTemplate", description = "短信模板对象")
public class SmsTemplate implements Serializable {
    private static final long serialVersionUID = -4572602655819179413L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "模板code")
    private String code;

    @ApiModelProperty(value = "模板内容")
    private String content;

    @ApiModelProperty(value = "创建时间", access = "hidden")
    private Long createTime;

    @ApiModelProperty(value = "更新时间", access = "hidden")
    private Long updateTime;
}
