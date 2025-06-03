package com.david.springbootmall.service.impl;

import com.david.springbootmall.dao.OrderDao;
import com.david.springbootmall.dao.ProductDao;
import com.david.springbootmall.dao.UserDao;
import com.david.springbootmall.dto.BuyItem;
import com.david.springbootmall.dto.CreateOrderRequest;
import com.david.springbootmall.model.Order;
import com.david.springbootmall.model.OrderItem;
import com.david.springbootmall.model.Product;
import com.david.springbootmall.model.User;
import com.david.springbootmall.service.OrderService;
import com.david.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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
        // 檢查User 是否存在
        User user = userDao.getUserById(userId);

        if (user == null) {
            logger.warn("該 userId {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalPrice = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            if (product == null ){
                logger.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (buyItem.getQuantity() > product.getStock()) {
                logger.warn("商品 {} 庫存數量不足, 無法購買。 剩餘庫存 {}, 欲購買數量 {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

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
