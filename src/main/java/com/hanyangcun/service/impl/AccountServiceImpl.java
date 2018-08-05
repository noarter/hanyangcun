package com.hanyangcun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.hanyangcun.component.SmsComponent;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.dao.IAccountDao;
import com.hanyangcun.dao.IActivityDao;
import com.hanyangcun.dao.ICouponRecordDao;
import com.hanyangcun.dao.ISmsTemplateDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.*;
import com.hanyangcun.request.CouponParam;
import com.hanyangcun.request.SendCouponParam;
import com.hanyangcun.service.IAccountService;
import com.hanyangcun.service.IOrderService;
import com.hanyangcun.util.GenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountDao accountDao;

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private ICouponRecordDao couponRecordDao;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ISmsTemplateDao smsTemplateDao;

    @Override
    public List<Account> getList(Account account) throws ErrorCodeException {
        return accountDao.getList(account);
    }

    @Override
    public Account get(String phone) throws ErrorCodeException {
        return accountDao.get(phone);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insert(Account account) throws ErrorCodeException {
        account.setCreateTime(System.currentTimeMillis());
        account.setUpdateTime(account.getCreateTime());
        accountDao.insert(account);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(Account account) throws ErrorCodeException {
        account.setUpdateTime(System.currentTimeMillis());
        accountDao.update(account);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void sendCoupon(SendCouponParam sendCouponParam) throws ErrorCodeException{

        //签名集合
        List<String> signNames = new ArrayList<>();

        //模版参数集合
        List<CouponParam> couponParams = new ArrayList<>();

        //领取记录集合
        List<CouponRecord> couponRecords = new ArrayList<>();

        String[] phones = sendCouponParam.getPhones();

        SmsTemplate smsTemplate = smsTemplateDao.get("赠送优惠券");

        if (smsTemplate==null)
            throw new ErrorCodeException(StatusCode.DATA_NOTFOUND.getCode(),StatusCode.DATA_NOTFOUND.getMsg());

        //1.通过优惠券活动id去查活动信息2.封装发送短信请求参数
        Activity activity = activityDao.get(sendCouponParam.getActivityId());

        if (activity==null)
            throw new ErrorCodeException(StatusCode.DATA_NOTFOUND.getCode(),StatusCode.DATA_NOTFOUND.getMsg());

        for (int i=0;i<phones.length;i++){
            signNames.add("韩养度假村");
            String couponNo = GenerateUtil.generateCouponNo();
            couponParams.add(new CouponParam(activity.getName(),couponNo,activity.getDiscountPrice()));
            CouponRecord couponRecord = new CouponRecord();
            couponRecord.setActivityId(sendCouponParam.getActivityId());
            couponRecord.setPhone(phones[i]);
            couponRecord.setCouponNo(couponNo);
            couponRecord.setCreateTime(System.currentTimeMillis());
            couponRecord.setUpdateTime(couponRecord.getCreateTime());
            couponRecords.add(couponRecord);
        }

        SendBatchSmsRequest sendBatchSmsRequest = new SendBatchSmsRequest();
        //使用post提交
        sendBatchSmsRequest.setMethod(MethodType.POST);
        //必填:待发送手机号。支持JSON格式的批量调用，批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        sendBatchSmsRequest.setPhoneNumberJson(JSONObject.toJSONString(phones));
        //必填:短信签名-支持不同的号码发送不同的短信签名
        sendBatchSmsRequest.setSignNameJson(JSONObject.toJSONString(signNames));
        //必填:短信模板-可在短信控制台中找到
        sendBatchSmsRequest.setTemplateCode(smsTemplate.getCode());
        //必填:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        sendBatchSmsRequest.setTemplateParamJson(JSONObject.toJSONString(couponParams));

        SendBatchSmsResponse sendBatchSmsResponse = null;
        try {
            sendBatchSmsResponse = smsComponent.SendBatchSms(sendBatchSmsRequest);
        } catch (ClientException e) {
            log.error("批量发送优惠券信息异常：",e.getErrMsg(),e);
            throw new ErrorCodeException(StatusCode.SEND_COUPON_NO_EXCEPTION.getCode(),StatusCode.SEND_COUPON_NO_EXCEPTION.getMsg());
        }

        //请求成功
        if (sendBatchSmsResponse.getCode() != null && sendBatchSmsResponse.getCode().equals("OK")) {
            //1-添加发送优惠券记录
            couponRecordDao.insertBatch(couponRecords);

            //2-更新领取优惠券数量
//            activity.setUseCount(activity.getUseCount()+couponRecords.size());
//            activity.setUpdateTime(System.currentTimeMillis());
//            activityDao.update(activity);
        }
    }

    @Override
    public Account getAccountOrder(String phone) throws ErrorCodeException {
        Account account = accountDao.get(phone);
        List<Order> orders = orderService.getOrderListByPhone(account.getPhone());
        account.setOrders(orders);
        return account;
    }

    @Override
    public List<Account> batchExport(String[] phones) throws ErrorCodeException {
        return accountDao.batchExport(phones);
    }
}
