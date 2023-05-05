package net.provera.orderserv.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.orderserv.event.OrderEvent;
import net.provera.orderserv.handler.OrderEventHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final OrderEventHandler orderEventHandler;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderEventListener(OrderEventHandler orderEventHandler, ObjectMapper objectMapper) {
        this.orderEventHandler = orderEventHandler;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void onOrderEvent(String orderEventJson) {
        try {
            OrderEvent orderEvent = objectMapper.readValue(orderEventJson, OrderEvent.class);
            orderEventHandler.handleEvent(orderEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process order event", e);
        }
    }
}
