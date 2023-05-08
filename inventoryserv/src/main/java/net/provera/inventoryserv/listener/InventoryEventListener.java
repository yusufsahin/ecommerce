package net.provera.inventoryserv.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.inventoryserv.event.InventoryEvent;
import net.provera.inventoryserv.handler.InventoryEventHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {

    private final InventoryEventHandler inventoryEventHandler;
    private final ObjectMapper objectMapper;

    @Autowired
    public InventoryEventListener(InventoryEventHandler inventoryEventHandler, ObjectMapper objectMapper) {
        this.inventoryEventHandler = inventoryEventHandler;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void onInventoryEvent(String inventoryEventJson) {
        try {
            InventoryEvent inventoryEvent = objectMapper.readValue(inventoryEventJson, InventoryEvent.class);
            inventoryEventHandler.handleEvent(inventoryEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process inventory event", e);
        }
    }
}
