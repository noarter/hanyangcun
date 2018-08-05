package com.hanyangcun.service.impl;

import com.hanyangcun.dao.ISmsTemplateDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.SmsTemplate;
import com.hanyangcun.service.ISmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SmsTemplateServiceImpl implements ISmsTemplateService {

    @Autowired
    private ISmsTemplateDao smsTemplateDao;

    @Override
    public List<SmsTemplate> getList(SmsTemplate smsTemplate) throws ErrorCodeException {
        return smsTemplateDao.getList(smsTemplate);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insert(SmsTemplate smsTemplate) throws ErrorCodeException {
        smsTemplate.setCreateTime(System.currentTimeMillis());
        smsTemplate.setUpdateTime(smsTemplate.getCreateTime());
        smsTemplateDao.insert(smsTemplate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(SmsTemplate smsTemplate) throws ErrorCodeException {
        smsTemplate.setUpdateTime(System.currentTimeMillis());
        smsTemplateDao.update(smsTemplate);
    }
}
