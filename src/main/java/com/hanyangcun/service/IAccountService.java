package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Account;

import java.util.List;

public interface IAccountService {
    List<Account> getList(Account account) throws ErrorCodeException;

    Account getById(Long id) throws ErrorCodeException;

    void insert(Account account) throws ErrorCodeException;

    void update(Account account) throws ErrorCodeException;
}
