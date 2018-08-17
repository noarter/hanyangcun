package com.hanyangcun.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.constant.SysConstants;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Order;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IOrderService;
import com.hanyangcun.util.HttpToken;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

@Api(description = "订单相关接口")
@RequestMapping("/order")
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "订单列表", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/getPagedList")
    public BaseResponse getPagedList(@RequestBody Order order, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token)) {
                return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
            }

            PageHelper.startPage(order.getPageNum(), order.getPageSize());
            List<Order> orders = orderService.getList(order);
            baseResponse.setData(new PageInfo<>(orders));
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取订单列表信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "获取订单信息", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/getOrderDetail/{orderNo}")
    public BaseResponse getOrderDetail(@PathVariable("orderNo") String orderNo) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            Order order = orderService.getOrderDetailByOrderNo(orderNo);
            baseResponse.setData(order);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("获取订单信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "保存订单", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 1001,message = "参数不正确"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @PostMapping("/saveOrder")
    public BaseResponse saveOrder(@RequestBody Order order) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (order.getInTime() == null || order.getOutTime() == null || order.getOrderType() == null
                    || StringUtils.isBlank(order.getGuests()) || StringUtils.isBlank(order.getGuestsPhone()) || order.getGuestsSex() == null)
                throw new ErrorCodeException(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
            orderService.insert(order);
            baseResponse.setData(order);
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("保存订单列表信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

    @ApiOperation(value = "取消订单", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "id", dataType = "Long", required = true, value = "订单id", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 1005, message = "入住时间前二天内，无法取消订单"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/cancelOrder/{id}")
    public BaseResponse cancelOrder(@PathVariable("id") Long id, HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            String token = HttpToken.getToken(request);
            if (StringUtils.isBlank(token))
                return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());

            if (id == null)
                return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());

            Order cancelOrder = orderService.getOrderDetailById(id);

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 2);

            if (cancelOrder.getInTime() < calendar.getTimeInMillis())
                throw new ErrorCodeException(StatusCode.IN_TIME_OUTRANGE.getCode(), StatusCode.IN_TIME_OUTRANGE.getMsg());

            if (SysConstants.ORDER_COMPLETE == cancelOrder.getOrderStatus()) {
                cancelOrder.setOrderStatus(SysConstants.ORDER_CANCEL);
                cancelOrder.setUpdateTime(System.currentTimeMillis());
                orderService.update(cancelOrder);
            }
        } catch (ErrorCodeException e) {
            baseResponse.setCode(e.getCode());
            baseResponse.setMsg(e.getMsg());
            log.error("取消订单信息失败：{}", e.getMsg(), e);
        }
        return baseResponse;
    }

}
