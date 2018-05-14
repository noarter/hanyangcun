package com.hanyangcun.service;

import com.hanyangcun.model.User;

public interface IUserService {
    User getUserByUsernameOrId(User user);
}
