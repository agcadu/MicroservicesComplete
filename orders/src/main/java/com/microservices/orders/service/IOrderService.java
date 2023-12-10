package com.microservices.orders.service;

import com.microservices.orders.dto.OrderRequest;
import com.microservices.orders.dto.OrderResponse;
import com.microservices.orders.model.Order;

import java.util.List;

public interface IOrderService {

    void placeOrder(OrderRequest orderRequest);

    public List<OrderResponse> getAllOrders();
}
