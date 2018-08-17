package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.PayRecord;

import java.util.Map;

public interface IPayRecordService {
    Map<String,Object> payOrder(PayRecord payRecord) throws ErrorCodeException;

    Map<String,Object> refundOrder(PayRecord payRecord) throws ErrorCodeException;

    /** 支付宝-成功后更改状态-清算中心错误 */
    String checkoutPay(String pay_no, String tradeNo, String totalAmount) throws ErrorCodeException;
}
