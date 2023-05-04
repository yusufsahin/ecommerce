package net.provera.orderserv.dao.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String productId;

    private String productName;
    private int quantity;
    private double price;

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
        this.price = quantity * this.price;
    }
}
