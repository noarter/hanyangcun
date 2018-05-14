package com.hanyangcun.service.impl;

import com.hanyangcun.dao.IUserDao;
import com.hanyangcun.model.User;
import com.hanyangcun.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public User getUserByUsernameOrId(User user) {
        return userDao.getUserByUsernameOrId(user);
    }
}
