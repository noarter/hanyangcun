package com.hanyangcun.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Account;
import com.hanyangcun.request.SendCouponParam;
import com.hanyangcun.response.AccountResponse;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IAccountService;
import com.hanyangcun.util.ExcelUtil;
import com.hanyangcun.util.HttpToken;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Api(description = "账户操作相关接口")
@RequestMapping("/account")
@Slf4j
@RestController
public class AccountController {

    @Autowired
    private IAccountService accountService;

    @ApiOperation(value = "获取账户列表信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/getPagedList")
    public BaseResponse getPagedList(@RequestBody Account account, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            PageHelper.startPage(account.getPageNum(), account.getPageSize());

            List<Account> accounts = accountService.getList(account);

            baseResponse.setData(new PageInfo<>(accounts));
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取账户列表信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "获取账户详情信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "账户id", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/get/{phone}")
    public BaseResponse<Account> get(@PathVariable("phone") String phone, HttpServletRequest request) {
        BaseResponse<Account> baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (StringUtils.isBlank(phone))
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            Account account = accountService.get(phone);
            baseResponse.setData(account);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取账户详情失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "获取账户下的订单信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "账户id", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/getAccountOrder/{phone}")
    public BaseResponse getAccountOrder(@PathVariable("phone") String phone, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (StringUtils.isBlank(phone))
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            Account account = accountService.getAccountOrder(phone);

            baseResponse.setData(account);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取账户详情失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "添加账户信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Account account, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            accountService.insert(account);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("添加账户信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "修改账户信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PatchMapping("/update")
    public BaseResponse update(@RequestBody Account account, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (StringUtils.isBlank(account.getPhone()) || StringUtils.isBlank(account.getName()))
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            accountService.update(account);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("修改账户信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "发送优惠券信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/sendCoupon")
    public BaseResponse sendCoupon(@RequestBody SendCouponParam sendCouponParam, HttpServletRequest request) throws Exception {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (sendCouponParam.getPhones() == null || sendCouponParam.getPhones().length < 1 || sendCouponParam.getActivityId() == null)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            accountService.sendCoupon(sendCouponParam);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("发送优惠券信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "批量导出账户信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/batchExport")
    public BaseResponse batchExport(@RequestBody String[] phones, HttpServletRequest request) throws Exception {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (phones == null || phones.length<1)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            List<Account> accounts = accountService.batchExport(phones);

            List<AccountResponse> accountResponses = new ArrayList<>();

            if (accounts!=null && accounts.size()>0){
                accounts.forEach(x->{
                    accountResponses.add(new AccountResponse(x.getName(),x.getPhone(),x.getSex()==0?"先生":(x.getSex()==1?"女士":"夫人")));
                });
            }

            String[][] headers = {{"姓名", "20"}, {"手机号", "30"}, {"性别", "10"}};
            // Excel输出地址
            OutputStream out = new FileOutputStream(new File("account.xls"));
            new ExcelUtil<AccountResponse>("账户信息表", headers, accountResponses, out).ExportExcel();
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("批量导出账户信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }
}