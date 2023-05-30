package net.provera.notificationserv.dao.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class Order {

    @Id
    private String id;

    private String customerId;

    private List<CartItem> orderItems;

    private Double totalAmount;

    private String status;
    public void addOrderItem(CartItem item) {
        orderItems.add(item);
    }

    public void removeOrderItem(CartItem item) {
        orderItems.remove(item);
    }
    public void updateOrderItem(CartItem item, int quantity) {
        Optional<CartItem> currentItem = orderItems.stream()
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
                .mapToDouble(CartItem::getPrice)
                .sum();
    }
}
