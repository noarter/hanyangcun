package com.hanyangcun.controller;

import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.enums.RoomStatus;
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

@RequestMapping("/rooms")
@Slf4j
@RestController
public class RoomsController {

    @Autowired
    private IRoomsService roomsService;

    @ApiOperation(value = "客房列表", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/getPagedList")
    public BaseResponse getPagedList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @ApiParam("查询条件:searchText") String searchText, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }
        Rooms rooms = new Rooms();
        if (StringUtils.isNumeric(searchText)) {
            rooms.setRoomNumber(Integer.valueOf(searchText));
        } else {
        }

        PageInfo<Rooms> pagedList = roomsService.getPagedList(pageIndex, pageSize, rooms);
        baseResponse.setData(pagedList);
        return baseResponse;
    }


    @ApiOperation(value = "客房上下架", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常"),
            @ApiResponse(code = 1004, message = "数据不存在"),
            @ApiResponse(code = 1001, message = "参数不正确")
    })
    @GetMapping("/modify/status")
    public BaseResponse modifyStatus(@ApiParam("房间id:id") Long id, HttpServletRequest request) {
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }
        if (id == null) {
            return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Rooms rooms = roomsService.get(id);
        if (rooms == null) {
            return new BaseResponse(StatusCode.DATA_NOTFOUND.getCode(), StatusCode.DATA_NOTFOUND.getMsg());
        }
        if (RoomStatus.ON_STOCK.getStatus().equals(rooms.getStatus())) {
            rooms.setStatus(RoomStatus.OUT_OF_STOCK.getStatus());
        } else {
            rooms.setStatus(RoomStatus.ON_STOCK.getStatus());
        }
        roomsService.update(rooms);
        return new BaseResponse(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg());
    }


    @ApiOperation(value = "客房修改信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常"),
            @ApiResponse(code = 1004, message = "数据不存在"),
            @ApiResponse(code = 1001, message = "参数不正确")
    })
    @GetMapping("/modify/type")
    public BaseResponse modifyType(@ApiParam("房间id:id") Long id, @ApiParam("房间类型:type") Integer type, @ApiParam("房间数量:roomNumber") Integer roomNumber, HttpServletRequest request) {
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }
        if (id == null || type == null || roomNumber == null) {
            return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Rooms rooms = roomsService.get(id);
        if (rooms == null) {
            return new BaseResponse(StatusCode.DATA_NOTFOUND.getCode(), StatusCode.DATA_NOTFOUND.getMsg());
        }
        rooms.setRoomNumber(roomNumber);
        rooms.setType(type);
        roomsService.update(rooms);
        return new BaseResponse(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg());
    }


    @ApiOperation(value = "修改客房折扣", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常"),
            @ApiResponse(code = 1004, message = "数据不存在"),
            @ApiResponse(code = 1001, message = "参数不正确")
    })
    @PostMapping("/modify/weekDate")
    public BaseResponse modifyWeekDate(@ApiParam("房间id数组:ids") Long[] ids, @ApiParam("折扣周期:weekDate") String weekDate, HttpServletRequest request) {
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }
        if (ids == null || weekDate == null) {
            return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
        }

        Rooms rooms = new Rooms();
        rooms.setWeekDate(weekDate);
        roomsService.updateBatch(rooms, Arrays.asList(ids));
        return new BaseResponse(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg());
    }

    @ApiOperation(value = "获取房间详情信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "房间", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 1001, message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/get/{id}")
    public BaseResponse<Rooms> get(@PathVariable("id") Long id, HttpServletRequest request) {
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse<>(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }

        if (id == null) {
            return new BaseResponse<>(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
        }

        Rooms rooms = roomsService.get(id);
        BaseResponse response = new BaseResponse();
        response.setData(rooms);
        response.setCode(StatusCode.SUCCESS.getCode());
        response.setMsg(StatusCode.SUCCESS.getMsg());
        return response;
    }

    @ApiOperation(value = "添加房间信息", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/insert")
    public BaseResponse insert(@RequestBody Rooms rooms) {
        BaseResponse baseResponse = new BaseResponse<>();
        roomsService.insert(rooms);
        baseResponse.setData(rooms);
        return baseResponse;
    }


}
