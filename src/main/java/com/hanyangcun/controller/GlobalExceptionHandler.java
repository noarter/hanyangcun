package com.hanyangcun.controller;

import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public BaseResponse defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        BaseResponse baseResponse = new BaseResponse();
        log.error("---DefaultException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost(), req.getRequestURL(), e);
        baseResponse.setCode(StatusCode.SYSTEM_FAILURE.getCode());
        baseResponse.setMsg(e.getMessage());
        return baseResponse;
    }
}
