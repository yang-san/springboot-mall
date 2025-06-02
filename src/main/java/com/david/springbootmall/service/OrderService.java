package com.david.springbootmall.service;

import com.david.springbootmall.dao.OrderDao;
import com.david.springbootmall.dto.CreateOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
