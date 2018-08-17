package com.hanyangcun.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.hanyangcun.config.AlipayConfig;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.PayRecord;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IPayRecordService;
import com.hanyangcun.util.IpUtils;
import com.hanyangcun.util.JsonUtils;
import com.hanyangcun.util.RequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Api(description = "支付相关接口")
@Slf4j
@RequestMapping("/pay")
@RestController
public class PayController {

    @Autowired
    private AlipayConfig alConf;
    @Autowired
    private IPayRecordService payRecordService;

    @ApiOperation(value = "支付接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 400, message = "缺少参数"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/payOrder")
    public BaseResponse payOrder(@RequestBody PayRecord payRecord,HttpServletRequest request,
                                 HttpServletResponse httpResponse) {
        log.info("传入参数：{}", JsonUtils.toJson(payRecord));
        BaseResponse baseResponse = new BaseResponse();
        try {
            if(payRecord.getSpbillCreateIp() == null){
                payRecord.setSpbillCreateIp(IpUtils.getIpAddr(request));
            }
            Map<String,Object> map = payRecordService.payOrder(payRecord);
            baseResponse.setData(map);
        }
        catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("订单支付失败：{}", e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "退款接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 400, message = "缺少参数"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/refundOrder")
    public BaseResponse refundOrder(@RequestBody PayRecord payRecord) {
        BaseResponse baseResponse = new BaseResponse();
        try {

            Map<String,Object> map = payRecordService.refundOrder(payRecord);
            baseResponse.setData(map);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("订单退款失败：{}", e.getMsg(), e);
        }

        return baseResponse;
    }

    @ApiOperation(value = "支付宝异步通知回调接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
//    @PostMapping("/aliNotifyOrder")
    @ResponseBody
    @RequestMapping(value = "aliNotifyOrder", method = { RequestMethod.POST, RequestMethod.GET })
    public void aliNotifyOrder(HttpServletRequest request, HttpServletResponse response)
            throws ErrorCodeException, IOException {
        // 支付宝响应消息
        String responseMsg = "";

        // 1. 解析请求参数
        Map<String, String> params = RequestUtil.getRequestParams(request);

        // 打印本次请求日志，开发者自行决定是否需要
        String result = JsonUtils.toJson(params);
        log.info("支付宝app返回串: {}", result.toString());
        if (!params.get("trade_status").equals("TRADE_SUCCESS")) {
            log.error("支付宝app支付失败: {}", params.get("memo"));
            throw new ErrorCodeException(500, "支付宝app支付失败.");
        }

        // 2. 验证签名
        try {
//            this.verifySign(params, alConf.getAlipayPublicKey(), alConf.getCharset(), alConf.getSignType());
            Boolean signVerified2 =false;
            Map<String, String> params0 = RequestUtil.getParameterMap(request);
            signVerified2 = AlipaySignature.rsaCheckV1(params0, alConf.getAlipayPublicKey(), alConf.getCharset(), alConf.getSignType()); //调用SDK验证签名
            if(!signVerified2){
                log.error("支付宝app支付验签失败，params：{}", result);
                throw new ErrorCodeException(500, "支付宝app支付验签失败.");
            }

        } catch (Exception e){
            log.error("支付宝app支付验签失败，params：{},error-info:{}", result, e);
            throw new ErrorCodeException(500, "支付宝支付验签失败.");
        }

        String tradeNo = params.get("trade_no");
        String paySn = params.get("out_trade_no");
        String totalAmount = params.get("total_amount");
        try {
            String resp2 = payRecordService.checkoutPay(paySn, tradeNo, totalAmount);
            JSONObject jsonObject = JSONObject.parseObject(resp2);
            Integer ob = jsonObject.getInteger("code");
            if (ob == null || ob != 200) {
                log.error("清算中心错误： error: {}", resp2);
                throw new ErrorCodeException(500, "支付宝支付失败.");
            }
            responseMsg = "{\"trade_status\":'\"TRADE_SUCCESS\"}";
        }catch (Exception e) {
            log.error("清算中心错误paySn:{}： error: {}", paySn, e);
            // 开发者可以根据异常自行进行处理
            throw new ErrorCodeException(500, "支付宝支付失败.");
        }
        // 5. 响应结果加签及返回
        try {
            responseMsg = encryptAndSign(responseMsg, alConf.getAlipayPublicKey(),
                    alConf.getGetMerchantPrivateKey(), alConf.getCharset(), false, true,
                    alConf.getSignType());
            // 开发者自行决定是否要记录，视自己需求
            log.info("开发者响应串responseMsg2: {}", responseMsg);
            // http 内容应答
            response.reset();
            response.setContentType("text/xml;charset="+alConf.getCharset());
            PrintWriter printWriter = response.getWriter();
            printWriter.print(responseMsg);
            response.flushBuffer();
        } catch (AlipayApiException alipayApiException) {
            // 开发者可以根据异常自行进行处理
            log.error("清算中心错误paySn:{}： error: {}", paySn, alipayApiException);
            // 开发者可以根据异常自行进行处理
            throw new ErrorCodeException(500, "支付宝支付失败.");
        }
    }

    @ApiOperation(value = "微信异步通知回调接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/wxNotifyOrder")
    public BaseResponse wxNotifyOrder() {
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
    }

    /** 加签*/
    public static String encryptAndSign(String bizContent, String alipayPublicKey, String cusPrivateKey, String charset,
                                        boolean isEncrypt, boolean isSign, String signType) throws AlipayApiException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(charset)) {
            charset = AlipayConstants.CHARSET_GBK;
        }
        sb.append("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
        if (isEncrypt) {// 加密
            sb.append("<alipay>");
            String encrypted = AlipaySignature.rsaEncrypt(bizContent, alipayPublicKey, charset);
            sb.append("<response>" + encrypted + "</response>");
            sb.append("<encryption_type>AES</encryption_type>");
            if (isSign) {
                String sign = AlipaySignature.rsaSign(encrypted, cusPrivateKey, charset, signType);
                sb.append("<sign>" + sign + "</sign>");
                sb.append("<sign_type>");
                sb.append(signType);
                sb.append("</sign_type>");
            }
            sb.append("</alipay>");
        } else if (isSign) {// 不加密，但需要签名
            sb.append("<alipay>");
            sb.append("<response>" + bizContent + "</response>");
            String sign = AlipaySignature.rsaSign(bizContent, cusPrivateKey, charset, signType);
            sb.append("<sign>" + sign + "</sign>");
            sb.append("<sign_type>");
            sb.append(signType);
            sb.append("</sign_type>");
            sb.append("</alipay>");
        } else {// 不加密，不加签
            sb.append(bizContent);
        }
        return sb.toString();
    }
}
