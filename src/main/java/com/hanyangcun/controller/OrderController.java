package com.hanyangcun.controller;

import com.github.pagehelper.PageInfo;
import com.hanyangcun.constant.StatusCode;
import com.hanyangcun.enums.OrderStatus;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Order;
import com.hanyangcun.response.BaseResponse;
import com.hanyangcun.service.IOrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @GetMapping("/getList")
    public BaseResponse getList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @ApiParam("查询条件:searchText") String searchText) throws ErrorCodeException {
        Order order = new Order();
        final int phoneNumberLength = 11;
        if (!StringUtils.isBlank(searchText)) {
            if (com.hanyangcun.util.StringUtils.isNumeric(searchText)) {
                if (searchText.length() > phoneNumberLength) {
                    order.setOrderNo(searchText);
                }
                if (searchText.length() == phoneNumberLength) {
                    order.setGuestsPhone(Integer.valueOf(searchText));
                }
            } else {
                order.setGuests(searchText);
            }
        }
        PageInfo<Order> list = orderService.getList(order, pageIndex, pageSize);
        BaseResponse response = new BaseResponse();
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "取消订单", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 1004, message = "数据不存在"),
            @ApiResponse(code = 1005, message = "入住时间前二天内，无法取消订单")
    })
    @GetMapping("/cancelOrder")
    public BaseResponse cancelOrder(@ApiParam("订单id：orderId") Long orderId) throws ErrorCodeException {
        if (orderId == null) {
            return new BaseResponse(StatusCode.NULL_ARGUMENT.getCode(), StatusCode.NULL_ARGUMENT.getMsg());
        }
        Order cancelOrder = orderService.getOrderDetailById(orderId);
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
    }
}
