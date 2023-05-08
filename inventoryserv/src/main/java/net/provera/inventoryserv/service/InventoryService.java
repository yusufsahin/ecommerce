package net.provera.inventoryserv.service;

import net.provera.inventoryserv.dao.model.Inventory;

public interface InventoryService {

    Inventory createInventory(Inventory inventory);

    Inventory updateInventory(Inventory inventory);

    void deleteInventory(String productId);

    Inventory getInventoryByProductId(String productId);
}
