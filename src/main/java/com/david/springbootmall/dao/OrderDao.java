package com.david.springbootmall.dao;

import com.david.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userId, Integer totalPrice);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
