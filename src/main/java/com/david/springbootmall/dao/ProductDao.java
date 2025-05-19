package com.david.springbootmall.dao;

import com.david.springbootmall.dto.ProductRequest;
import com.david.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
}
