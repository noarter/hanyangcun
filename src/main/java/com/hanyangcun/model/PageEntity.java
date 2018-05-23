package com.hanyangcun.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "PageEntity", description = "分页对象")
public class PageEntity implements Serializable {
    private static final long serialVersionUID = 3477839989070371046L;

    @ApiModelProperty(value = "当前页数")
    private Integer pageNum;

    @ApiModelProperty(value = "当前页显示条数")
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}