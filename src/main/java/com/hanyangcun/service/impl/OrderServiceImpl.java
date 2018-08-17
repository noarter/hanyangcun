package com.hanyangcun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.hanyangcun.component.SmsComponent;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.dao.IAccountDao;
import com.hanyangcun.dao.IOrderDao;
import com.hanyangcun.dao.ISmsTemplateDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Account;
import com.hanyangcun.model.Order;
import com.hanyangcun.model.SmsTemplate;
import com.hanyangcun.service.IOrderService;
import com.hanyangcun.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hanyangcun.util.date.DateUtil.timeStamp2Date;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IAccountDao accountDao;

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private ISmsTemplateDao smsTemplateDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insert(Order order) throws ErrorCodeException {
        if(order.getActualAmount() == null || order.getActualAmount() <= 0f){
            throw new ErrorCodeException(400, "订单号有误,金额为0");
        }
        order.setOrderNo(GenerateUtil.generateOrderNo());
        order.setOrderTime(System.currentTimeMillis());
        order.setUpdateTime(order.getOrderTime());
        orderDao.insert(order);

        List<Account> accounts = new ArrayList<>();
        long curTime = System.currentTimeMillis();
        if (StringUtils.isNotBlank(order.getLinkName()) && StringUtils.isNotBlank(order.getLinkPhone()) && order.getLinkSex() != null) {
            Account account = new Account();
            account.setName(order.getLinkName());
            account.setPhone(order.getLinkPhone());
            account.setSex(order.getLinkSex());
            account.setCreateTime(curTime);
            account.setUpdateTime(curTime);
            accounts.add(account);
        }
        Account ac = new Account();
        ac.setName(order.getGuests());
        ac.setPhone(order.getGuestsPhone());
        ac.setSex(order.getGuestsSex());
        ac.setCreateTime(curTime);
        ac.setUpdateTime(curTime);
        accounts.add(ac);
        accountDao.insertBatch(accounts);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(Order order) throws ErrorCodeException {
        order.setUpdateTime(System.currentTimeMillis());
        orderDao.update(order);

        try {
            SmsTemplate smsTemplate = smsTemplateDao.get("取消订单");

            if (smsTemplate == null)
                throw new ErrorCodeException(StatusCode.DATA_NOTFOUND.getCode(), StatusCode.DATA_NOTFOUND.getMsg());

            Map map = new HashMap();
            map.put("name", order.getGuests());
            map.put("time", timeStamp2Date(order.getInTime(), "yyyy-MM-dd") + "-" + timeStamp2Date(order.getOutTime(), "yyyy-MM-dd"));
            map.put("Day", order.getNights());
            map.put("Huxing", order.getOrderType());
            map.put("number", order.getOrderNo());

            SendSmsRequest request = new SendSmsRequest();
            request.setSignName("韩养度假村");
            request.setMethod(MethodType.POST);
            request.setTemplateCode(smsTemplate.getCode());
            request.setPhoneNumbers(order.getGuestsPhone());
            request.setTemplateParam(JSONObject.toJSONString(map));

            smsComponent.sendSms(request);
        } catch (ClientException e) {
            log.error("取消订单发送短信异常:{}", e, e.getErrCode() + ":" + e.getErrMsg());
            throw new ErrorCodeException(StatusCode.SYSTEM_FAILURE.getCode(), e.getErrMsg());
        }
    }



    @Override
    public Order getOrderDetailByOrderNo(String orderNO) throws ErrorCodeException {
        return orderDao.getOrderDetailByOrderNo(orderNO);
    }

    @Override
    public Order getOrderDetailById(Long id) throws ErrorCodeException {
        return orderDao.getOrderDetailById(id);
    }

    @Override
    public List<Order> getList(Order order) throws ErrorCodeException {
        return orderDao.getList(order);
    }

    @Override
    public List<Order> getOrderListByPhone(String phone) throws ErrorCodeException {
        List<Order> orders = orderDao.getOrderListByPhone(phone);
        return orders;
    }
}