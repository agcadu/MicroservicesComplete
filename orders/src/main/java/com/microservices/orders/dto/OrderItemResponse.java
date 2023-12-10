package com.microservices.orders.dto;

public record OrderItemResponse (Long id, String sku, Double price, Long quantity){
}
