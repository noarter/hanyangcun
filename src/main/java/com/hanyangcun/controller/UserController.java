package com.hanyangcun.controller;

import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.User;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IUserService;
import com.hanyangcun.util.HttpToken;
import com.hanyangcun.util.JWTUtil;
import com.hanyangcun.util.SHA512Util;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(description = "用户操作相关接口")
@RequestMapping("/user")
@Slf4j
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "登陆", notes = "")
    @PostMapping("/login")
    public BaseResponse<Map> login(@RequestBody User user, Device device) {
        BaseResponse<Map> baseResponse = new BaseResponse<>();

        String username = user.getUsername();
        String password = user.getPassword();
        try {
            User userInfo = userService.getUserByUsernameOrId(user);
            if (userInfo == null)
                throw new ErrorCodeException(StatusCode.USER_NOT_EXIST.getCode(), StatusCode.USER_NOT_EXIST.getMsg());

            String encPassword = SHA512Util.encry512(password + user.getUsername() + userInfo.getSalt());

            if (!userInfo.getPassword().equals(encPassword))
                throw new ErrorCodeException(StatusCode.PASSWORD_ERROR.getCode(), StatusCode.PASSWORD_ERROR.getMsg());

            // 验证用户名密码成功后生成token
            String token = JWTUtil.createToken(username, device);

            Map map = new HashMap();

            map.put("access_token", token);

            baseResponse.setData(map);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("登录账户信息异常:{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "刷新token", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/token/refresh")
    public BaseResponse<Map> refreshToken(HttpServletRequest request) {
        BaseResponse<Map> baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            String newToken = JWTUtil.refreshToken(token);
            Map map = new HashMap();
            map.put("refresh_token", newToken);
            baseResponse.setData(map);
        } catch (Exception e) {
            baseResponse.setCode(500);
            baseResponse.setMsg(e.getMessage());
            log.error("登录账户信息异常:{}", e.getMessage(), e);
        }
        return baseResponse;
    }

}
