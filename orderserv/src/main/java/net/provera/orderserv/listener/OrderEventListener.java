package net.provera.orderserv.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.orderserv.event.OrderEvent;
import net.provera.orderserv.event.OrderEventDto;
import net.provera.orderserv.event.OrderEventType;
import net.provera.orderserv.handler.OrderEventHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;


@Component
public class OrderEventListener {

    private final OrderEventHandler orderEventHandler;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderEventListener(OrderEventHandler orderEventHandler,RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.orderEventHandler = orderEventHandler;
        this.objectMapper = objectMapper;
        this.rabbitTemplate=rabbitTemplate;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void onOrderEvent(@Payload String orderEventJson) {
        System.out.println("Order Event came");
        processOrder(orderEventJson);
    }
    private void processOrder(String orderEventJson){
        try{
            OrderEventDto cartEventDto = objectMapper.readValue(orderEventJson,OrderEventDto.class);
            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrder(cartEventDto.getOrder());
            orderEvent.setEventType(cartEventDto.getEventType().equals("CREATED")?OrderEventType.CREATED:
                    cartEventDto.getEventType().equals("UPDATED")?OrderEventType.UPDATED:OrderEventType.DELETED);;
            orderEventHandler.handleEvent(orderEvent);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Failed to process cart event", e);
        }
    }
}
