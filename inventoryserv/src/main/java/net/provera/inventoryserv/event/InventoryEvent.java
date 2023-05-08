package net.provera.inventoryserv.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.provera.inventoryserv.dao.model.Inventory;

@Data

@NoArgsConstructor
public class InventoryEvent {
    private Inventory inventory;
    private InventoryEventType eventType;

    public InventoryEvent(Inventory inventory, InventoryEventType eventType) {
        this.inventory = inventory;
        this.eventType = eventType;
    }

    // Other methods
}
