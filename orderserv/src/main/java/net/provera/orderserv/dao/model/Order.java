package net.provera.orderserv.dao.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import java.util.List;
import java.util.Optional;

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
        Optional<OrderItem> currentItem = orderItems.stream()
                .filter(i -> i.getProductId().equals(item.getProductId()))
                .findFirst();

        currentItem.ifPresent(orderItem -> {
            orderItem.setQuantity(quantity);
            orderItem.setPrice(quantity * orderItem.getPrice());
            updateTotalAmount();
        });
    }

    public void updateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
    }
}
