package com.microservices.orders.service;

import com.microservices.orders.dto.*;
import com.microservices.orders.events.OrderEvent;
import com.microservices.orders.model.Order;
import com.microservices.orders.model.OrderItems;
import com.microservices.orders.model.enums.OrderStatus;
import com.microservices.orders.repository.IOrderRepository;
import com.microservices.orders.utils.JsonUtils;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService{

    private final IOrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObservationRegistry observationRegistry;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        Observation inventoryObservation = Observation.createNotStarted("inventory-service", observationRegistry);

        return inventoryObservation.observe(() -> {
            BaseResponse result = webClientBuilder.build().post()
                    .uri("lb://inventory-service/api/inventory/in-stock")
                    .bodyValue(orderRequest.getOrderItems())
                    .retrieve()
                    .bodyToMono(BaseResponse.class)
                    .block();

            if (result != null && !result.hasErrors()) {

                Order order = new Order();
                order.setOrderNumber(UUID.randomUUID().toString());
                order.setOrderItems(orderRequest.getOrderItems().stream()
                        .map(orderItemRequest -> mapOrderItemToOrderItem(orderItemRequest, order)).toList());
                var savedOrder = orderRepository.save(order);

                //kafka producer send message to order topic
                kafkaTemplate.send("orders-topic", JsonUtils.toJson(new OrderEvent(savedOrder.getOrderNumber(),
                        savedOrder.getOrderItems().size(), OrderStatus.PLACED)));

                return mapOrderToOrderResponse(savedOrder);
            } else {
                throw new IllegalArgumentException("Some items are not in stock");
            }
        });
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(this::mapOrderToOrderResponse).toList();
    }

    private OrderResponse mapOrderToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber(),
                order.getOrderItems().stream().map(this::mapOrderToOrderItemRequest).toList());
    }

    private OrderItemResponse mapOrderToOrderItemRequest(OrderItems orderItems) {
        return new OrderItemResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }

    private OrderItems mapOrderItemToOrderItem(OrderItemRequest orderItemRequest, Order order) {
        return OrderItems.builder()
                .id(orderItemRequest.getId())
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }
}
