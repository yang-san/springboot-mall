package com.david.springbootmall.dao;

import com.david.springbootmall.model.Order;
import com.david.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalPrice);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

}
