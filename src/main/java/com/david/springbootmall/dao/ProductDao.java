package com.david.springbootmall.dao;

import com.david.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
