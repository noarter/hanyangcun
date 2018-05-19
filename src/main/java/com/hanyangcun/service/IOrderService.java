package com.hanyangcun.service;

import com.github.pagehelper.PageInfo;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Order;

import java.util.List;

public interface IOrderService {
    void insert(Order order) throws ErrorCodeException;

    void update(Order order) throws ErrorCodeException;

    PageInfo<Order> getPagedList(Order order, Integer pageIndex, Integer pageSize) throws ErrorCodeException;

    Order getOrderDetailByOrderNo(String orderNO) throws ErrorCodeException;

    Order getOrderDetailById(Long id) throws ErrorCodeException;
}
