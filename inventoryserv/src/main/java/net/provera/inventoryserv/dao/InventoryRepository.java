package net.provera.inventoryserv.dao;

import java.util.List;

import net.provera.inventoryserv.dao.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductId(String productId);
}