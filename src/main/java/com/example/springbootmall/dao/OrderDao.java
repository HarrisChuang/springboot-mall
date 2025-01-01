package com.example.springbootmall.dao;

import com.example.springbootmall.dto.CreateOrderRequest;
import com.example.springbootmall.dto.OrderQueryParams;
import com.example.springbootmall.model.Order;
import com.example.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer countOrders(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemsById(Integer orderId);
    Integer createOrder(Integer userId, Integer totalAmount);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
