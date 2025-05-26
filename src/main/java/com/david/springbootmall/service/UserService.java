package com.david.springbootmall.service;

import com.david.springbootmall.dto.UserLoginRequest;
import com.david.springbootmall.dto.UserRegisterRequest;
import com.david.springbootmall.model.User;

public interface UserService {
    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User login(UserLoginRequest userLoginRequest);
}
