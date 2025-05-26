package com.david.springbootmall.service.impl;

import com.david.springbootmall.dao.UserDao;
import com.david.springbootmall.dto.UserLoginRequest;
import com.david.springbootmall.dto.UserRegisterRequest;
import com.david.springbootmall.model.User;
import com.david.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if (user != null) {
            logger.warn("email {} 已經被註冊 ", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Integer userId = userDao.createUser(userRegisterRequest);
        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        User user = userDao.getUserById(userId);
        return user;
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if (user == null) {
            logger.warn("email {} 尚未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email 尚未註冊");
        }

        if (user.getPassword().equals(userLoginRequest.getPassword())) {
            return user;
        } else {
            logger.warn("email {} 密碼錯誤", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼錯誤");
        }
    }
}
