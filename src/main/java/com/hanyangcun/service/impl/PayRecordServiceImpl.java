package com.hanyangcun.service.impl;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.PayRecord;
import com.hanyangcun.service.IPayRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PayRecordServiceImpl implements IPayRecordService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> payOrder(PayRecord payRecord) throws ErrorCodeException {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void refundOrder(PayRecord payRecord) throws ErrorCodeException {

    }
}
