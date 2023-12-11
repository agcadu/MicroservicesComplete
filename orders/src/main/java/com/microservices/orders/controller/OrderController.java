package com.microservices.orders.controller;

import com.microservices.orders.dto.OrderRequest;
import com.microservices.orders.dto.OrderResponse;
import com.microservices.orders.service.IOrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/place")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "order-service", fallbackMethod = "placeOrderFallback")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        var orders = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(orders);

    }

    public ResponseEntity<OrderResponse> placeOrderFallback(OrderRequest orderRequest, Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getOrders() {
        return orderService.getAllOrders();
    }
}
