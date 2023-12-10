package com.microservices.inventory.service;


import com.microservices.inventory.dto.BaseResponse;
import com.microservices.inventory.dto.OrderItemRequest;

import java.util.List;

public interface IInventoryService {
    boolean isInStock(String sku);

    BaseResponse areInStock(List<OrderItemRequest> orderItems);
}
