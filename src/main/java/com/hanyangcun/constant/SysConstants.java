package com.hanyangcun.constant;

public class SysConstants {

    /**
     * 删除标识位 0- 正常 1- 已删除
     */
    public static final int STATUS_UN_DELETED = 0;
    public static final int STATUS_DELETED = 1;

    /**
     * 账户锁定状态 0- 未锁定 1- 已锁定
     */
    public static final int STATUS_UN_LOCKED = 0;
    public static final int STATUS_LOCKED = 1;

    /**
     * 状态 0-上架 1-下架
     */
    public static final int STATUS_ONLINE = 0;
    public static final int STATUS_OFFLINE = 1;

    /**
     * 订单状态 0-待支付 1-已支付 2-已取消
     */
    public static final int ORDER_PAY = 0;
    public static final int ORDER_COMPLETE = 1;
    public static final int ORDER_CANCEL = 2;

    /**
     * 活动状态：1-未开始2-已开始3-已结束
     */
    public static final int ACTIVITY_START = 1;
    public static final int ACTIVITY_STARTING = 2;
    public static final int ACTIVITY_END = 3;

}
