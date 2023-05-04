package net.provera.orderserv.dao.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    private String id;

    @Field
    private String customerId;

    @Field
    private List<OrderItem> orderItems;

    @Field
    private double totalAmount;

    @Field
    private String status;
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
    }

    public void updateOrderItem(OrderItem item, int quantity) {
        OrderItem currentItem = orderItems.stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst()
                .orElse(null);

        if (currentItem != null) {
            currentItem.setQuantity(quantity);
            currentItem.setPrice(quantity * currentItem.getPrice());
            updateTotalAmount();
        }
    }

    public void updateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
    }
}
