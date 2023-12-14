package com.microservices.orders.events;

import com.microservices.orders.model.enums.OrderStatus;

public record OrderEvent(String orderNumber, int itemsCount, OrderStatus orderStatus) {
}
