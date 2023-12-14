package com.microservices.notificationserver.events;


import com.microservices.notificationserver.model.enums.OrderStatus;

public record OrderEvent(String orderNumber, int itemsCount, OrderStatus orderStatus) {
}
