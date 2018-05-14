package com.hanyangcun.service.impl;

import com.hanyangcun.dao.IOrderDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Order;
import com.hanyangcun.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao orderDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void insert(Order order) throws ErrorCodeException {
        orderDao.insert(order);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(Order order) throws ErrorCodeException{
        orderDao.update(order);
    }

    @Override
    public List<Order> getList(Order order) throws ErrorCodeException {
        return orderDao.getList(order);
    }

    @Override
    public Order getOrderDetailByOrderNo(String orderNO) throws ErrorCodeException {
        return orderDao.getOrderDetailByOrderNo(orderNO);
    }

    @Override
    public Order getOrderDetailById(Long id) throws ErrorCodeException {
        return orderDao.getOrderDetailById(id);
    }
}