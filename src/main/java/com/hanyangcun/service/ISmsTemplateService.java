package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.SmsTemplate;

import java.util.List;

public interface ISmsTemplateService {
    List<SmsTemplate> getList(SmsTemplate smsTemplate) throws ErrorCodeException;

    void insert(SmsTemplate smsTemplate) throws ErrorCodeException;

    void update(SmsTemplate smsTemplate) throws ErrorCodeException;
}
