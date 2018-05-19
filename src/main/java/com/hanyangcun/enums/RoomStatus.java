package com.hanyangcun.enums;

public enum RoomStatus {
    OUT_OF_STOCK(2, "下架"),
    ON_STOCK(1, "上架");
    private Integer status;
    private String description;

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    RoomStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
}
