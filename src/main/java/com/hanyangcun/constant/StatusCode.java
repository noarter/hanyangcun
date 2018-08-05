package com.hanyangcun.constant;

public enum StatusCode {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 系统错误
     */
    TOKEN_VALID(401, "TOKEN失效"),

    /**
     * 系统错误
     */
    SYSTEM_FAILURE(500, "服务异常"),

    /**
     * 参数不正确
     */
    ILLEGAL_ARGUMENT(1001, "参数不正确"),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(1002, "用户不存在"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(1003, "密码错误"),

    /**
     * 数据不存在
     */
    DATA_NOTFOUND(1004, "数据不存在"),

    /**
     * 超过入住时间
     */
    IN_TIME_OUTRANGE(1005, "入住时间前二天内，无法取消订单"),

    /**
     * 数据已存在
     */
    DATA_IS_EXIST(1006,"数据已经存在"),

    /**
     *
     */
    SEND_COUPON_NO_EXCEPTION(2000,"批量发送优惠券异常"),
    ;

    /**
     * 枚举值
     */
    private int code;

    /**
     * 枚举信息
     */
    private String msg;

    private StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
