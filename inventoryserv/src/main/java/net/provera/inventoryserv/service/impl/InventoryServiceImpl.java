package net.provera.inventoryserv.service.impl;

import net.provera.inventoryserv.dao.InventoryRepository;
import net.provera.inventoryserv.dao.model.Inventory;
import net.provera.inventoryserv.event.InventoryEvent;
import net.provera.inventoryserv.event.InventoryEventType;
import net.provera.inventoryserv.service.InventoryService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, RabbitTemplate rabbitTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        Inventory savedInventory = inventoryRepository.save(inventory);
        publishInventoryEvent(savedInventory, "inventory.created");
        return savedInventory;
    }

    @Override
    public Inventory updateInventory(Inventory inventory) {
        Optional<Inventory> inventoryDb = Optional.ofNullable(inventoryRepository.findByProductId(inventory.getProductId()));
        if (inventoryDb.isPresent()) {
            Inventory existingInventory = inventoryDb.get();
            existingInventory.setLocation(inventory.getLocation());
            existingInventory.setQuantity(inventory.getQuantity());
            Inventory updatedInventory = inventoryRepository.save(existingInventory);
            publishInventoryEvent(updatedInventory, "inventory.updated");
            return updatedInventory;
        }
        return null;
    }

    @Override
    public void deleteInventory(String productId) {
        Optional<Inventory> inventoryDb = Optional.ofNullable(inventoryRepository.findByProductId(productId));
        inventoryDb.ifPresent(inventory -> {
            inventoryRepository.deleteById(inventory.getId());
            publishInventoryEvent(inventory, "inventory.deleted");
        });
    }

    @Override
    public Inventory getInventoryByProductId(String productId) {
        Optional<Inventory> inventoryDb = Optional.ofNullable(inventoryRepository.findByProductId(productId));
        return inventoryDb.orElse(null);
    }

    private void publishInventoryEvent(Inventory inventory, String routingKey) {
        InventoryEvent inventoryEvent = new InventoryEvent(inventory, routingKey.equals("inventory.updated") ? InventoryEventType.UPDATED : InventoryEventType.DELETED);
        rabbitTemplate.convertAndSend("inventory", routingKey, inventoryEvent);
    }
}
