package com.hanyangcun.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.constant.SysConstants;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Rooms;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IRoomsService;
import com.hanyangcun.util.HttpToken;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Api(description = "客房操作相关接口")
@RequestMapping("/rooms")
@Slf4j
@RestController
public class RoomsController {

    @Autowired
    private IRoomsService roomsService;

    @ApiOperation(value = "获取客房列表", notes = "", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/getList")
    public BaseResponse getList() {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Rooms rooms = new Rooms();
            rooms.setStatus(SysConstants.STATUS_ONLINE);
            List<Rooms> roomsList = roomsService.getList(rooms);
            baseResponse.setData(roomsList);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取客房列表信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "分页获取客房列表", notes = "", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/getPagedList")
    public BaseResponse getPagedList(@RequestBody Rooms rooms, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            PageHelper.startPage(rooms.getPageNum(), rooms.getPageSize());

            List<Rooms> roomsList = roomsService.getList(rooms);

            baseResponse.setData(new PageInfo<>(roomsList));
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("分页获取客房列表信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "获取客房详情信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "房间id", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/get/{id}")
    public BaseResponse<Rooms> get(@PathVariable("id") Long id, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                return new BaseResponse<>(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (id == null)
                return new BaseResponse<>(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            Rooms rooms = roomsService.get(id);

            baseResponse.setData(rooms);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取客房信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "添加客房信息", notes = "", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Rooms rooms, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                throw new ErrorCodeException(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            roomsService.insert(rooms);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("添加客房信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }


    @ApiOperation(value = "修改客房信息", notes = "", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常"),
            @ApiResponse(code = 1001, message = "参数不正确")
    })
    @PatchMapping("/update")
    public BaseResponse update(@RequestBody Rooms rooms, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token)) {
                return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
            }
            if (rooms.getId() == null) {
                return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
            }
            roomsService.update(rooms);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("修改客房信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }


    @ApiOperation(value = "批量修改客房折扣", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "query", name = "ids", dataType = "Long[]", required = true, value = "客房id集合", defaultValue = ""),
            @ApiImplicitParam(paramType = "query", name = "weekDate", dataType = "String", required = true, value = "周期", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常"),
            @ApiResponse(code = 1004, message = "数据不存在"),
            @ApiResponse(code = 1001, message = "参数不正确")
    })
    @PostMapping("/batchUpdateWeekDate")
    public BaseResponse batchUpdateWeekDate(@RequestParam("ids") Long[] ids, @RequestParam("weekDate") String weekDate, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token)) {
                return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
            }
            if (ids == null || StringUtils.isBlank(weekDate)) {
                return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
            }
            roomsService.updateBatch(weekDate, Arrays.asList(ids));
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("批量修改客房折扣失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

}
