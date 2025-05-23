package com.david.springbootmall.dao;

import com.david.springbootmall.dto.UserRegisterRequest;
import com.david.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);

}
