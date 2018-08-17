package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Account;
import com.hanyangcun.request.SendCouponParam;

import java.util.List;

public interface IAccountService {
    List<Account> getList(Account account) throws ErrorCodeException;

    Account get(String phone) throws ErrorCodeException;

    void insert(Account account) throws ErrorCodeException;

    void update(Account account) throws ErrorCodeException;

    void sendCoupon(SendCouponParam sendCouponParam)  throws ErrorCodeException;

    Account getAccountOrder(String phone) throws ErrorCodeException;

    List<Account> batchExport(String[] phones) throws ErrorCodeException;
}
