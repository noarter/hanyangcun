package com.hanyangcun.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.hanyangcun.component.SmsComponent;
import com.hanyangcun.config.AlipayConfig;
import com.hanyangcun.config.WXpayConfig;
import com.hanyangcun.constant.PayConstant;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.dao.IOrderDao;
import com.hanyangcun.dao.IPayRecordDao;
import com.hanyangcun.dao.ISmsTemplateDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Order;
import com.hanyangcun.model.PayRecord;
import com.hanyangcun.model.SmsTemplate;
import com.hanyangcun.service.IPayRecordService;
import com.hanyangcun.util.GenerateUtil;
import com.hanyangcun.util.HttpClientContext;
import com.hanyangcun.util.HttpUtil;
import com.hanyangcun.util.JsonUtils;
import com.hanyangcun.util.date.DateUtil;
import com.hanyangcun.wxpay.WXPayConstants;
import com.hanyangcun.wxpay.WXPayModel;
import com.hanyangcun.wxpay.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hanyangcun.util.date.DateUtil.timeStamp2Date;

@Slf4j
@Service
public class PayRecordServiceImpl implements IPayRecordService {

    @Autowired
    private WXpayConfig wxPayConfig;
    @Autowired
    private IPayRecordDao payRecordDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private AlipayConfig alConf;

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private ISmsTemplateDao smsTemplateDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> payOrder(PayRecord payRecord) throws ErrorCodeException {

        Map<String, Object> map = new HashMap<String, Object>();
        String orderNo = payRecord.getOrderNo();
        Integer type = payRecord.getType();

        if (orderNo == null || type == null) {
            throw new ErrorCodeException(400, "订单号或者支付方式为空");
        }

        Order order = orderDao.getOrderDetailByOrderNo(orderNo);
        if (order == null) {
            throw new ErrorCodeException(400, "订单号有误");
        }

        payRecord.setCreateTime(DateUtil.getLongTimeStamp());
        payRecord.setPayState(0);
        payRecord.setPayNo(GenerateUtil.generatePayNo());
        int i = payRecordDao.insert(payRecord);
        if (i < 1) {
            log.error("新增支付单出现异常：{}");
            throw new ErrorCodeException(500, "下单失败");
        }
        Float actualAmount = order.getActualAmount();
        if (actualAmount == null || actualAmount <= 0) {
            log.error("订单金额为空/0");
            throw new ErrorCodeException(500, "下单失败");
        }

        WXPayModel payModel = new WXPayModel();

        DecimalFormat fnum = new DecimalFormat("##0.00");
        String dd = fnum.format(actualAmount);
        String bizContent;
        String form;

        String url = "https://" + WXPayConstants.DOMAIN_API;
        try {
            switch (type) {
                case 1:
                    long time = System.currentTimeMillis();
                    bizContent = "{" +
                            "    \"out_trade_no\":\"" + payRecord.getPayNo() + "\"," +
                            "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                            "    \"total_amount\":" + dd + "," +
                            "    \"subject\":\"" + PayConstant.TITLE + "\"," +
                            "    \"body\":\"" + PayConstant.TITLE + "\"" +
                            "  }";
//                    bizContent.put("out_trade_no", payRecord.getPayNo());
//                    bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
//                    bizContent.put("total_amount", dd);
//                    bizContent.put("subject", "\""+PayConstant.TITLE+"\"");
//                    bizContent.put("body", "\""+PayConstant.TITLE+"\"");
//                    bizContent.put("passback_params", payRecord.getPayNo());
//                    bizContent.put("extend_params", "{\"sys_service_provider_id\":\""+new Date().toString()+"\"}");
                    form = getPageForm(bizContent);
                    map.put("form", form);
                    break;
                case 2:
                    bizContent = "{" +
                            " \"out_trade_no\":\"" + payRecord.getPayNo() + "\"," +
                            " \"total_amount\":\"" + dd + "\"," +
                            " \"subject\":\"" + PayConstant.TITLE + "\"," +
                            " \"product_code\":\"QUICK_WAP_PAY\"" +
                            " }";
//                    bizContent.put("out_trade_no", "\""+payRecord.getPayNo()+"\"");
//                    bizContent.put("total_amount", "\""+dd+"\"");
//                    bizContent.put("subject", "\""+PayConstant.TITLE+"\"");
//                    bizContent.put("product_code", "QUICK_WAP_PAY");
                    form = getWapForm(bizContent);
                    map.put("form", form);
                    break;
                case 3:
                    payModel.setNonce_str(WXPayUtil.generateNonceStr());
                    payModel.setBody(PayConstant.TITLE);
                    payModel.setOut_trade_no(payRecord.getPayNo());
                    payModel.setTotal_fee(Integer.valueOf(actualAmount.toString()));
                    payModel.setSpbill_create_ip(payRecord.getSpbillCreateIp());
                    payModel.setTrade_type("NATIVE");
                    map = getWXPayReqStr(url, payModel, wxPayConfig);
                    break;
                case 4:
                    payModel.setNonce_str(WXPayUtil.generateNonceStr());
                    payModel.setBody(PayConstant.TITLE);
                    payModel.setOut_trade_no(payRecord.getPayNo());
                    payModel.setTotal_fee(Integer.valueOf(actualAmount.toString()));
                    payModel.setSpbill_create_ip(payRecord.getSpbillCreateIp());
                    payModel.setTrade_type("JSAPI");
                    map = getWXPayReqStr(url, payModel, wxPayConfig);
                    break;
                case 5:
                    payModel.setNonce_str(WXPayUtil.generateNonceStr());
                    payModel.setBody(PayConstant.TITLE);
                    payModel.setOut_trade_no(payRecord.getPayNo());
                    payModel.setTotal_fee(Integer.valueOf(actualAmount.toString()));
                    payModel.setSpbill_create_ip(payRecord.getSpbillCreateIp());
                    payModel.setTrade_type("MWEB");
                    payModel.setScene_info("{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://07132871188.com\",\"wap_name\": \"" + PayConstant.TITLE + "\"}}");
                    url = url + WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
                    map = getWXPayReqStr(url, payModel, wxPayConfig);
                    break;
                default:
                    throw new ErrorCodeException(400, "支付方式异常");
            }
            return map;
        } catch (Exception e) {
            log.error("下支付单出现异常：{}", e);
            throw new ErrorCodeException(500, "下单失败");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> refundOrder(PayRecord payRecord) throws ErrorCodeException {
        Map<String, Object> map = new HashMap<>();
        String pay_no = payRecord.getPayNo();
        String orderNo = payRecord.getOrderNo();
        if (pay_no == null && orderNo == null) {
            log.warn("未获取到pay_no/orderNo, ");
            throw new ErrorCodeException(400, "支付单/订单号错误");
        }
        if (pay_no != null) {
            payRecord = payRecordDao.getPayDetailByPayNo(pay_no);
            if (payRecord == null || payRecord.getOrderNo() == null || payRecord.getPayState() != 1) {
                log.warn("未获取到orderNo, pay_no:{}", pay_no);
                throw new ErrorCodeException(400, "支付单/订单号错误");
            }
            orderNo = payRecord.getOrderNo();
        } else {
            List<PayRecord> payRecords = payRecordDao.getPayDetailByOrderNo(orderNo);

            if (payRecords == null || payRecords.size() < 1) {
                log.warn("未获取到orderNo, pay_no:{}", pay_no);
                throw new ErrorCodeException(400, "支付单/订单号错误");
            }
            int num = 0;
            for (PayRecord payRecord1 : payRecords) {
                if (payRecord1 != null && payRecord1.getOrderNo() != null && payRecord1.getPayState() == 1) {
                    num++;
                    payRecord = payRecord1;
                    if (num > 1) {
                        log.error("此订单有多条支付成功记录,orderNo：{}, payNo:{}", orderNo, pay_no);
                    }
                }
            }
            orderNo = payRecord.getOrderNo();
            pay_no = payRecord.getPayNo();
        }
        if (orderNo == null) {
            log.error("订单为空，error:{}", "");
            throw new ErrorCodeException(400, "支付单/订单号错误");
        }

        Order order = orderDao.getOrderDetailByOrderNo(orderNo);
        if (order == null) {
            log.warn("未获取到order, orderNo:{}", payRecord.getOrderNo());
            throw new ErrorCodeException(400, "支付单/订单号错误");
        }
        if (order.getOrderStatus() != 1) {
            log.info("订单未支付成功, OrderNo:{}", payRecord.getOrderNo());
            throw new ErrorCodeException(400, "此订单/订单号未支付");
        }

        Integer type = payRecord.getType();
        PayRecord payRecord1 = new PayRecord();
        String dd = "0";
        if (type != null && type < 3) {
            DecimalFormat fnum = new DecimalFormat("##0.00");
            dd = fnum.format(order.getActualAmount());
            String outRequestNo = GenerateUtil.generateNo("3");
            String bizContent = "{" +
                    " \"out_trade_no\":\"" + pay_no + "\"," +
                    " \"out_request_no\":\"" + outRequestNo + "\"," +
                    " \"refund_amount\":\"" + dd + "\"" +
                    " }";//设置业务参数
            log.info("bizContent: {}", bizContent);
            try {
                String body = getTradeRefundForm(bizContent);
                log.info("支付宝退款出参body：{}", body);
                try {
                    JSONObject bodyJson = JSONObject.parseObject(body);
                    bodyJson = bodyJson.getJSONObject("alipay_trade_refund_response");
                    String sub_msg = bodyJson.getString("sub_msg");
                    if (!bodyJson.getString("code").equals("10000")) {
                        log.error(" 退款失败,payNo: {}, body:{}", pay_no, body);
                        throw new ErrorCodeException(500, sub_msg);
                    }
                    String refund_fee = bodyJson.getString("refund_fee");
                    if (!dd.equals(refund_fee)) {
                        log.error("支付退款失败(金额退款不一致)payNo: {}, error:{}", pay_no, "{" + dd + " != " + refund_fee + "}");
                        throw new ErrorCodeException(500, "退款失败");
                    }
                } catch (Exception e) {
                    log.error("退款异常error:{}", e);
                    throw new ErrorCodeException(500, "退款失败");
                }

            } catch (Exception e) {
                log.error("支付退款失败payNo: {}，error{}", pay_no, e);
                throw new ErrorCodeException(500, "退款失败");
            }
            payRecord1.setOutRequestNo(outRequestNo);
        } else {//微信支付退款
            throw new ErrorCodeException(400, "退款失败(暂时不支持)");
        }
        payRecord1.setPayNo(pay_no);
        payRecord1.setUpdateTime(DateUtil.getLongTimeStamp());
        payRecord1.setPayState(2);
        int u = payRecordDao.update(payRecord1);
        if (u < 1) {
            log.error("支付单退款出现异常,pay_no：{}", pay_no);
            throw new ErrorCodeException(500, "支付宝app退款失败.");
        }
        Order order1 = new Order();
        order1.setOrderNo(orderNo);
        order1.setOrderStatus(2);
        order1.setUpdateTime(DateUtil.getLongTimeStamp());
        u = orderDao.update(order1);
        if (u < 1) {
            log.error("订单退款异常,pay_no：{}", pay_no);
            throw new ErrorCodeException(500, "支付宝app退款失败.");
        }

        //退款成功发送短信
        try {
            SmsTemplate smsTemplate = smsTemplateDao.get("退款通知");

            if (smsTemplate == null)
                throw new ErrorCodeException(StatusCode.DATA_NOTFOUND.getCode(), StatusCode.DATA_NOTFOUND.getMsg());

            Map m = new HashMap();
            m.put("name", order.getGuests());
            m.put("code", order.getOrderNo());

            SendSmsRequest request = new SendSmsRequest();
            request.setSignName("韩养度假村");
            request.setMethod(MethodType.POST);
            request.setTemplateCode(smsTemplate.getCode());
            request.setPhoneNumbers(order.getGuestsPhone());
            request.setTemplateParam(JSONObject.toJSONString(m));

            SendSmsResponse sendSmsResponse = smsComponent.sendSms(request);

            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK"))
                //请求成功
                log.info("退款成功发送短信结束:"+sendSmsResponse.getCode()+"=>"+sendSmsResponse.getMessage());
            else
                log.info("退款成功发送短信结束:"+sendSmsResponse.getCode()+"=>"+sendSmsResponse.getMessage());
        } catch (ClientException e) {
            log.error("退款通知发送短信异常:{}", e, e.getErrCode() + ":" + e.getErrMsg());
            throw new ErrorCodeException(StatusCode.SYSTEM_FAILURE.getCode(), e.getErrMsg());
        }
        map.put("refund_fee", dd);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public String checkoutPay(String pay_no, String tradeNo, String totalAmount) throws ErrorCodeException {

        PayRecord payRecord = payRecordDao.getPayDetailByPayNo(pay_no);
        if (payRecord == null || payRecord.getOrderNo() == null) {
            log.warn("未获取到orderNo, pay_no:{}", pay_no);
            return "{\"code\":500,\"msg\":\"下单失败\"}";
        }
        if (payRecord.getPayState() == 1) {
            log.info("支付单已支付成功, pay_no:{}", pay_no);
            return "{\"code\":200,\"msg\":\"下单失败\"}";
        }

        Order order = orderDao.getOrderDetailByOrderNo(payRecord.getOrderNo());
        if (order == null) {
            log.warn("未获取到order, OrderNo:{}", payRecord.getOrderNo());
            return "{\"code\":500,\"msg\":\"下单失败\"}";
        }
        if (order.getOrderStatus() == 1 || order.getOrderStatus() == 3) {
            log.info("订单已支付成功/退款, OrderNo:{}", payRecord.getOrderNo());
            return "{\"code\":200,\"msg\":\"下单失败\"}";
        }

        payRecord.setPayNo(pay_no);
        payRecord.setUpdateTime(DateUtil.getLongTimeStamp());
        payRecord.setPayState(1);
        payRecord.setTradeNo(tradeNo);
        int u = payRecordDao.update(payRecord);
        if (u < 1) {
            log.error("支付单出现异常,pay_no：{}", pay_no);
            return "{\"code\":500,\"msg\":\"下单失败\"}";
        }

        Order order1 = new Order();
        order1.setOrderNo(payRecord.getOrderNo());
        order1.setOrderStatus(1);
        order1.setUpdateTime(DateUtil.getLongTimeStamp());
        u = orderDao.update(order1);
        if (u < 1) {
            log.error("支付单出现异常,pay_no：{}", pay_no);
            return "{\"code\":500,\"msg\":\"下单失败\"}";
        }

        if (payRecord.getType() < 3) {//支付宝支付
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String dd = fnum.format(order.getActualAmount());
            if (dd == null || totalAmount == null || Double.valueOf(dd) > Double.valueOf(totalAmount)) {
                log.error("支付宝app支付通知信息金额被修改： paySn:{} ", pay_no);
                throw new ErrorCodeException(500, "支付宝app支付失败.");
            }
        }

        try {
            //发送下单成功消息
            SmsTemplate smsTemplate = smsTemplateDao.get("预定成功短信通知");

            if (smsTemplate == null)
                throw new ErrorCodeException(StatusCode.DATA_NOTFOUND.getCode(), StatusCode.DATA_NOTFOUND.getMsg());

            Map map = new HashMap();
            map.put("name", order.getGuests());
//            map.put("time", timeStamp2Date(order.getInTime(), "yyyy-MM-dd") + "-" + timeStamp2Date(order.getOutTime(), "yyyy-MM-dd"));
            map.put("time1", timeStamp2Date(order.getInTime(), "yyyy-MM-dd"));
            map.put("time2", timeStamp2Date(order.getOutTime(), "yyyy-MM-dd"));
            map.put("Day", order.getNights());
            map.put("Huxing", order.getOrderType());
            map.put("number", order.getOrderNo());

            SendSmsRequest request = new SendSmsRequest();
            request.setSignName("韩养度假村");
            request.setMethod(MethodType.POST);
            request.setTemplateCode(smsTemplate.getCode());
            request.setPhoneNumbers(order.getGuestsPhone());
            request.setTemplateParam(JSONObject.toJSONString(map));
            SendSmsResponse sendSmsResponse = smsComponent.sendSms(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK"))
                //请求成功
                log.info("支付成功发送短信结束:"+sendSmsResponse.getCode()+"=>"+sendSmsResponse.getMessage());
            else
                log.info("支付成功发送短信失败:"+sendSmsResponse.getCode()+"=>"+sendSmsResponse.getMessage());

        } catch (ClientException e) {
            log.error("下单成功发送短信异常:{}", e, e.getErrCode() + ":" + e.getErrMsg());
            throw new ErrorCodeException(StatusCode.SYSTEM_FAILURE.getCode(), e.getErrMsg());
        }

        return "{\"code\":200,\"msg\":\"下单成功\",\"data\":\"\"}";
    }

    /**
     * 支付宝手机/网站浏览器支付
     */
    private String getWapForm(String bizContent) {
        AlipayClient alipayClient = new DefaultAlipayClient(alConf.getGatewayUrl(), alConf.getAppId(), alConf.getGetMerchantPrivateKey(), "json", alConf.getCharset(), alConf.getAlipayPublicKey(), alConf.getSignType()); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(alConf.getReturnUrl());
        alipayRequest.setNotifyUrl(alConf.getNotifyUrl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent(bizContent);
        /* 填充业务参数*/
        log.info("手机-填充业务参数:{}", alipayRequest.getBizContent());
        String form = "";
        try {

            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            form = "支付宝支付订单异常";
            log.error("{}, 订单信息：{}", form, bizContent);
            e.printStackTrace();
        }
        log.info("form:{}", form);
        return form;
    }

    /**
     * 支付宝-网页支付
     */
    private String getPageForm(String bizContent) {
        AlipayClient alipayClient = new DefaultAlipayClient(alConf.getGatewayUrl(), alConf.getAppId(), alConf.getGetMerchantPrivateKey(), "json", alConf.getCharset(), alConf.getAlipayPublicKey(), alConf.getSignType()); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl(alConf.getReturnUrl());
        alipayRequest.setNotifyUrl(alConf.getNotifyUrl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent(bizContent);
        /* 填充业务参数*/
        log.info("网页-填充业务参数:{}", alipayRequest.getBizContent());
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            form = "支付宝支付订单异常";
            log.error("{}, 订单信息：{}", form, bizContent);
            e.printStackTrace();
        }
        log.info("form:{}", form);
        return form;
    }

    /**
     * 支付宝-退款
     */
    private String getTradeRefundForm(String bizContent) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(alConf.getGatewayUrl(), alConf.getAppId(), alConf.getGetMerchantPrivateKey(), "json", alConf.getCharset(), alConf.getAlipayPublicKey(), alConf.getSignType()); //获得初始化的AlipayClient
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
        request.setBizContent(bizContent);//设置业务参数
        AlipayTradeRefundResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        String body = response.getBody();
        log.info("body:{}", body);
        return body;
    }

    /**
     * 微信-统一下单请求报文
     */
    private Map<String, Object> getWXPayReqStr(String url, WXPayModel payModel, WXpayConfig wxPayConfig) throws Exception {
        Map<String, Object> mapResult = new HashMap<String, Object>();

        Map<String, Object> map = new HashMap<>();
        map.put("sign_type", wxPayConfig.getSign_type());
        map.put("appid", wxPayConfig.getAppid());
        map.put("limit_pay", wxPayConfig.getLimit_pay());
        map.put("mch_id", wxPayConfig.getMch_id());
        map.put("notify_url", wxPayConfig.getNotify_url());
        map.putAll(JsonUtils.objectToJson(payModel));
        String xmlStr = WXPayUtil.generateSignedXml((Map) map, wxPayConfig.getKey());

        String result = HttpUtil.httpsRequest(url, "POST", xmlStr);
        if (result == null) {
            log.error("下单后结果为空");
            throw new ErrorCodeException(500, "结果为空");
        }
        Map<String, String> data = WXPayUtil.xmlToMap(result);
        if (data == null || data.size() < 1 || data.get("return_code") == null || !data.get("return_code").equals("SUCCESS")) {
            log.error("下单后结果不合法:{}", result);
            throw new ErrorCodeException(500, "结果不合法");
        }
        WXPayConstants.SignType signType;
        if (WXPayConstants.MD5.equals(wxPayConfig.getSign_type())) {
            signType = WXPayConstants.SignType.MD5;
        } else {
            signType = WXPayConstants.SignType.HMACSHA256;
        }
        Boolean isSign = WXPayUtil.isSignatureValid(data, wxPayConfig.getKey(), signType);
        if (!isSign) {
            log.error("下单后对结果验证签名失败:{}", result);
            throw new ErrorCodeException(500, "结果验证签名失败");
        }

        if (data.get("result_code") == null || !data.get("result_code").equals("SUCCESS")) {
            log.error("下单后对业务结果失败:{}", result);
            throw new ErrorCodeException(500, "业务结果失败");
        }

        String mweb_url = data.get("mweb_url");
        if (mweb_url != null) {
            map.put("mweb_url", mweb_url);
        }
        String prepay_id = data.get("prepay_id");
        if (mweb_url != null) {
            map.put("prepay_id", prepay_id);
        }
        String code_url = data.get("code_url");
        if (mweb_url != null) {
            map.put("code_url", code_url);
        }
        return map;
    }
}
