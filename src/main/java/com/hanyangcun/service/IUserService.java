package com.hanyangcun.service;

import com.hanyangcun.exception.ErrorCodeException;
import com.hanyangcun.model.User;

import java.util.List;

public interface IUserService {

    List<User> getList(User user) throws ErrorCodeException;

    User get(User user) throws ErrorCodeException;

    void insert(User user) throws ErrorCodeException;

    void update(User user) throws ErrorCodeException;
}
