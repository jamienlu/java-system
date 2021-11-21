package com.asura.provider.api;

import com.asura.services.User;
import com.asura.services.UserService;

import java.io.Serializable;

public class UserServiceImpl implements UserService, Serializable {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
