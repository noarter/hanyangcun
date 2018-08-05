package com.hanyangcun.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.constant.SysConstants;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Activity;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IActivityService;
import com.hanyangcun.util.HttpToken;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "活动相关接口")
@RequestMapping("/activity")
@Slf4j
@RestController
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    @ApiOperation(value = "分页获取活动列表信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/getPagedList")
    public BaseResponse getPagedList(@RequestBody Activity activity, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            PageHelper.startPage(activity.getPageNum(), activity.getPageSize());

            List<Activity> activities = activityService.getList(activity);

            baseResponse.setData(new PageInfo<>(activities));
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("分页获取活动列表失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "获取活动列表信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/getList")
    public BaseResponse getList(HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            Activity activity = new Activity();
            activity.setActivityStatus(SysConstants.ACTIVITY_STARTING);

            List<Activity> activities = activityService.getList(activity);

            baseResponse.setData(activities);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取活动列表失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "获取活动详情信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "活动id", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/get/{id}")
    public BaseResponse<Activity> get(@PathVariable("id") Long id, HttpServletRequest request) {
        BaseResponse<Activity> baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (id == null)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            Activity activity = activityService.getById(id);
            baseResponse.setData(activity);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取活动详情失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "添加活动信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Activity activity,HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            activityService.insert(activity);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("新增活动失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "复制活动信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/copy/{id}")
    public BaseResponse copy(@PathVariable("id") Long id, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (id == null)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            activityService.copy(id);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("复制活动失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "更新活动信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PatchMapping("/update")
    public BaseResponse update(@RequestBody Activity activity, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (activity.getId() == null)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            activityService.update(activity);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("更新活动失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "删除活动信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "活动id", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @DeleteMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable("id") Long id, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (id == null)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            activityService.delete(id);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("删除活动失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }
}
