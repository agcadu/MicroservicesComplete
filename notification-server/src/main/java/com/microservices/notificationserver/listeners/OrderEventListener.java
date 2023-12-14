package com.microservices.notificationserver.listeners;

import com.microservices.notificationserver.events.OrderEvent;
import com.microservices.notificationserver.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {

    @KafkaListener(topics = "orders-topic")
    public void handleOOrdersNotification(String message) {
        var orderEvent = JsonUtils.fromJson(message, OrderEvent.class);

        //Enviar notificación a los usuarios o a otrod microservicios que lo requieran

        log.info("Order {} event received for order : {} with {} items", orderEvent.orderStatus(), orderEvent.orderNumber(), orderEvent.itemsCount());
    }

}
