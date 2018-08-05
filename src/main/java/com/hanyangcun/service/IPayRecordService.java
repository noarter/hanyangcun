package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.PayRecord;

import java.util.Map;

public interface IPayRecordService {
    Map<String,Object> payOrder(PayRecord payRecord) throws ErrorCodeException;

    void refundOrder(PayRecord payRecord) throws ErrorCodeException;
}
