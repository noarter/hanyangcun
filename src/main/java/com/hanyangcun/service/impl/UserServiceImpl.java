package com.hanyangcun.service.impl;

import com.hanyangcun.constant.SysConstants;
import com.hanyangcun.dao.IUserDao;
import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.User;
import com.hanyangcun.service.IUserService;
import com.hanyangcun.util.GenerateUtil;
import com.hanyangcun.util.SHA512Util;
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
    public User getByName(String username) {
        return userDao.getByName(username);
    }

    @Override
    public User getById(Long id) throws ErrorCodeException {
        return userDao.getById(id);
    }

    @Override
    public void insert(User user) throws ErrorCodeException {
        String salt = GenerateUtil.generateSalt();
        String encPassword = SHA512Util.encry512(user.getPassword() + user.getUsername() + salt);
        user.setPassword(encPassword);
        user.setSalt(salt);
        user.setLocked(SysConstants.STATUS_UN_LOCKED);
        user.setCreateTime(new Date());
        user.setUpdateTime(user.getCreateTime());
        userDao.insert(user);
    }

    @Override
    public void update(User user) throws ErrorCodeException {
        user.setUpdateTime(new Date());
        userDao.update(user);
    }
}
