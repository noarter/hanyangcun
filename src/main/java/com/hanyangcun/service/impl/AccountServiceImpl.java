package com.hanyangcun.service.impl;

import com.hanyangcun.dao.IAccountDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.Account;
import com.hanyangcun.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountDao accountDao;

    @Override
    public List<Account> getList(Account account) throws ErrorCodeException {
        return accountDao.getList(account);
    }

    @Override
    public Account getById(Long id) throws ErrorCodeException {
        return accountDao.getById(id);
    }

    @Override
    public void insert(Account account) throws ErrorCodeException {
        account.setCreateTime(new Date());
        account.setUpdateTime(account.getCreateTime());
        accountDao.insert(account);
    }

    @Override
    public void update(Account account) throws ErrorCodeException {
        account.setUpdateTime(new Date());
        accountDao.update(account);
    }
}
