package net.provera.inventoryserv.dao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Getter @Setter @NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private String productId;

    private Integer quantity;

    private String location;

    public Inventory(String productId, Integer quantity, String location) {
        this.productId = productId;
        this.quantity = quantity;
        this.location = location;
    }
}
