package com.hanyangcun.controller;

import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.enums.OrderStatus;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Order;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IOrderService;
import com.hanyangcun.util.HttpToken;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Api(description = "订单接口")
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
    @GetMapping("/getPagedList")
    public BaseResponse getPagedList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @ApiParam("查询条件:searchText") String searchText, HttpServletRequest request) {
        Order order = new Order();
        BaseResponse response = new BaseResponse();
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }
        final int phoneNumberLength = 11;
        if (!StringUtils.isBlank(searchText)) {
            if (StringUtils.isNumeric(searchText)) {
                if (searchText.length() > phoneNumberLength) {
                    order.setOrderNo(searchText);
                }
                if (searchText.length() <= phoneNumberLength) {
                    order.setGuestsPhone(Integer.valueOf(searchText));
                }
            } else {
                order.setGuests(searchText);
            }
        }
        PageInfo<Order> list = null;
        try {
            list = orderService.getPagedList(order, pageIndex, pageSize);
            response.setData(list);
        } catch (ErrorCodeException e) {
            e.printStackTrace();
            response.setCode(e.getCode());
            response.setMsg(e.getMsg());
        }
        return response;
    }

    @ApiOperation(value = "取消订单", notes = "")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "header", name = "access_token", dataType = "String", required = true, value = "token", defaultValue = "")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 1004, message = "数据不存在"),
            @ApiResponse(code = 1005, message = "入住时间前二天内，无法取消订单"),
            @ApiResponse(code = 401, message = "TOKEN失效"),
            @ApiResponse(code = 500, message = "服务异常")
    })
    @GetMapping("/cancelOrder")
    public BaseResponse cancelOrder(@ApiParam("订单id：orderId") Long orderId, HttpServletRequest request) {
        String token = HttpToken.getToken(request);
        if (StringUtils.isBlank(token)) {
            return new BaseResponse(StatusCode.TOKEN_VALID.getCode(), StatusCode.TOKEN_VALID.getMsg());
        }
        if (orderId == null) {
            return new BaseResponse(StatusCode.ILLEGAL_ARGUMENT.getCode(), StatusCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Order cancelOrder = null;
        try {
            cancelOrder = orderService.getOrderDetailById(orderId);
            if (cancelOrder == null) {
                return new BaseResponse(StatusCode.DATA_NOTFOUND.getCode(), StatusCode.DATA_NOTFOUND.getMsg());
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 2);
            if (cancelOrder.getInTime().after(calendar.getTime())) {
                if (OrderStatus.PAID.getStatus().equals(cancelOrder.getOrderStatus()) || OrderStatus.WAIT_PAY.getStatus().equals(cancelOrder.getOrderStatus())) {
                    cancelOrder.setOrderStatus(OrderStatus.CANCEL.getStatus());
                    cancelOrder.setUpdateTime(new Date());
                    orderService.update(cancelOrder);
                }
                return new BaseResponse(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg());
            } else {
                return new BaseResponse(StatusCode.IN_TIME_OUTRANGE.getCode(), StatusCode.IN_TIME_OUTRANGE.getMsg());
            }
        } catch (ErrorCodeException e) {
            e.printStackTrace();
            return new BaseResponse(e.getCode(), e.getMsg());
        }
    }
}
