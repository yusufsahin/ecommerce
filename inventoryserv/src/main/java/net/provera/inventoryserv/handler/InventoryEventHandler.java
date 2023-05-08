package net.provera.inventoryserv.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.inventoryserv.dao.model.Inventory;
import net.provera.inventoryserv.event.InventoryEvent;
import net.provera.inventoryserv.event.InventoryEventType;
import net.provera.inventoryserv.service.InventoryService;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventHandler {
    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    public InventoryEventHandler(InventoryService inventoryService, ObjectMapper objectMapper) {
        this.inventoryService = inventoryService;
        this.objectMapper = objectMapper;
    }

    public void handleEvent(InventoryEvent event) {
        Inventory inventory = event.getInventory();
        InventoryEventType eventType = event.getEventType();

        switch (eventType) {
            case UPDATED:
                Inventory existingInventory = inventoryService.getInventoryByProductId(inventory.getProductId());
                if (existingInventory != null) {
                    existingInventory.setQuantity(inventory.getQuantity());
                    existingInventory.setLocation(inventory.getLocation());
                    inventoryService.updateInventory(existingInventory);
                } else {
                    inventoryService.createInventory(inventory);
                }
                break;
            case DELETED:
                inventoryService.deleteInventory(inventory.getProductId());
                break;
            default:
                throw new IllegalArgumentException("Invalid event type");
        }
    }

    public void handleInventoryEvent(String inventoryEventJson) {
        try {
            InventoryEvent inventoryEvent = objectMapper.readValue(inventoryEventJson, InventoryEvent.class);
            handleEvent(inventoryEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process inventory event", e);
        }
    }
}
