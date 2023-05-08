package net.provera.cartserv.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.cartserv.event.CartEvent;
import net.provera.cartserv.handler.CartEventHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartEventListener {
    private final CartEventHandler cartEventHandler;
    private final ObjectMapper objectMapper;
    @Autowired
    CartEventListener(CartEventHandler cartEventHandler,ObjectMapper objectMapper){
        this.cartEventHandler=cartEventHandler;
        this.objectMapper=objectMapper;
    }
    @RabbitListener(queues = "cart-queue")
    public void onCartEvent(String cartEventJson) {
        try {
            CartEvent cartEvent = objectMapper.readValue(cartEventJson, CartEvent.class);
            cartEventHandler.handleEvent(cartEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process cart event", e);
        }
    }
}
