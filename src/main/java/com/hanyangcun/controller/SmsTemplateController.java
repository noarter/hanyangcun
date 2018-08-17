package com.hanyangcun.controller;

import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.SmsTemplate;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.ISmsTemplateService;
import com.hanyangcun.util.HttpToken;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "短信模板相关接口")
@RequestMapping("/smsTemplate")
@Slf4j
@RestController
public class SmsTemplateController {

    @Autowired
    private ISmsTemplateService smsTemplateService;

    @ApiOperation(value = "获取短信模板列表信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/getList")
    public BaseResponse getList(@RequestBody SmsTemplate smsTemplate, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            List<SmsTemplate> smsTemplates = smsTemplateService.getList(smsTemplate);

            baseResponse.setData(smsTemplates);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取短信模板列表信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "保存短信模板", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 1001,message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/insert")
    public BaseResponse saveSmsTemplate(@RequestBody SmsTemplate smsTemplate, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (StringUtils.isBlank(smsTemplate.getName()) || StringUtils.isBlank(smsTemplate.getCode()))
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            SmsTemplate sms = new SmsTemplate();
            sms.setName(smsTemplate.getName());
            List<SmsTemplate> smsTemplates = smsTemplateService.getList(sms);

            if (!CollectionUtils.isEmpty(smsTemplates))
                throw new ErrorCodeException(StatusCode.DATA_IS_EXIST.getCode(), StatusCode.DATA_IS_EXIST.getMsg());

            smsTemplateService.insert(smsTemplate);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("保存短信模板信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "修改短信模板信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PatchMapping("/update")
    public BaseResponse update(@RequestBody SmsTemplate smsTemplate, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (smsTemplate.getId()==null || StringUtils.isBlank(smsTemplate.getCode()))
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            smsTemplateService.update(smsTemplate);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("修改短信模板信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }
}