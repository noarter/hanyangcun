package com.hanyangcun.controller;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.PayRecord;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IPayRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(description = "支付相关接口")
@Slf4j
@RequestMapping("/pay")
@RestController
public class PayController {

    @Autowired
    private IPayRecordService payRecordService;

    @ApiOperation(value = "支付接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/payOrder")
    public BaseResponse payOrder(@RequestBody PayRecord payRecord) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Map<String,Object> map = payRecordService.payOrder(payRecord);
            baseResponse.setData(map);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("订单支付失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "退款接口", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/refundOrder")
    public BaseResponse refundOrder(@RequestBody PayRecord payRecord) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            payRecordService.refundOrder(payRecord);
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
    @PostMapping("/aliNotifyOrder")
    public BaseResponse aliNotifyOrder() {
        BaseResponse baseResponse = new BaseResponse();
        return baseResponse;
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
}
