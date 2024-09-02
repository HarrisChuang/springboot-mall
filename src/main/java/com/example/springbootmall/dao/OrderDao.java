package com.example.springbootmall.dao;

import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
