package com.hanyangcun.service.impl;

import com.hanyangcun.dao.IUserDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.User;
import com.hanyangcun.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public List<User> getList(User user) throws ErrorCodeException {
        return userDao.getList(user);
    }

    @Override
    public User get(User user) throws ErrorCodeException {
        return userDao.get(user);
    }

    @Override
    public void insert(User user) throws ErrorCodeException {
        userDao.insert(user);
    }

    @Override
    public void update(User user) throws ErrorCodeException {
        user.setUpdateTime(new Date());
        userDao.update(user);
    }
}
