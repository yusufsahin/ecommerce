package net.provera.cartserv.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.cartserv.event.CartEvent;
import net.provera.cartserv.event.CartEventDto;
import net.provera.cartserv.event.CartEventType;
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
            CartEventDto cartEventDto = objectMapper.readValue(cartEventJson, CartEventDto.class);
            CartEvent cartEvent = new CartEvent(cartEventDto.getUserId(),cartEventDto.getCartItems(),
            cartEventDto.getEventType().equals("ITEMADDED") ? CartEventType.ITEMADDED :
            cartEventDto.getEventType().equals("ITEMREMOVED")? CartEventType.ITEMREMOVED:
            cartEventDto.getEventType().equals("ITEMUPDATED")?CartEventType.ITEMUPDATED:
            cartEventDto.getEventType().equals("CLEARED")?CartEventType.CLEARED: CartEventType.ORDERED);
            System.out.println(cartEvent.getEventType().toString());
            cartEventHandler.handleEvent(cartEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process cart event", e);
        }
    }
}
