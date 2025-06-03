package com.david.springbootmall.service.impl;

import com.david.springbootmall.dao.OrderDao;
import com.david.springbootmall.dao.ProductDao;
import com.david.springbootmall.dto.BuyItem;
import com.david.springbootmall.dto.CreateOrderRequest;
import com.david.springbootmall.model.Order;
import com.david.springbootmall.model.OrderItem;
import com.david.springbootmall.model.Product;
import com.david.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);
        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalPrice = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            // 計算總價錢
            int productPrice = product.getPrice() * buyItem.getQuantity();
            totalPrice += productPrice;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(productPrice);

            orderItemList.add(orderItem);

        }
        // 創建訂單
        Integer orderId = orderDao.createOrder(userId, totalPrice);
        orderDao.createOrderItems(orderId, orderItemList);
        return orderId;
    }
}
