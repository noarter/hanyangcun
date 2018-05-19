package com.hanyangcun.enums;

public enum OrderStatus {
    DEFAULT(0, "默认状态"),
    WAIT_PAY(1, "待支付"),
    PAID(2, "已支付"),
    CANCEL(3, "已取消");
    private Integer status;
    private String description;

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    OrderStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
