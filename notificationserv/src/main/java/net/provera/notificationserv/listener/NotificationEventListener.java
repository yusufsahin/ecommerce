package net.provera.notificationserv.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.provera.notificationserv.event.CartEventDto;
import net.provera.notificationserv.event.OrderEventDto;
import net.provera.notificationserv.handler.NotificationEventHandler;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class NotificationEventListener {
    private final NotificationEventHandler notificationEventHandler;
    private final Gson objectMapper;
    @Autowired
    NotificationEventListener(NotificationEventHandler notificationEventHandler, Gson objectMapper){
        this.notificationEventHandler = notificationEventHandler;
        this.objectMapper=objectMapper;

    }
    @RabbitListener(queues = "cart-queue")
    public void onCartEvent(String cartEventJson) {
        try {
            CartEventDto cartEventDto = objectMapper.fromJson(cartEventJson, CartEventDto.class);
            notificationEventHandler.handleCartEvent(cartEventDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process cart event", e);
        }
    }
    @RabbitListener(queues = "order-queue")
    public void onOrderEvent(String orderEventJson) {
        try {
            OrderEventDto orderEventDto = objectMapper.fromJson(orderEventJson.substring(1,orderEventJson.length()-1).translateEscapes(),OrderEventDto.class);
            notificationEventHandler.handleOrderEvent(orderEventDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process order event", e);
        }
    }
}
