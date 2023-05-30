package net.provera.notificationserv.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.provera.notificationserv.dao.model.CartItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartEventDto {
    private String userId;
    private List<CartItem> cartItems;
    private String eventType;
}
