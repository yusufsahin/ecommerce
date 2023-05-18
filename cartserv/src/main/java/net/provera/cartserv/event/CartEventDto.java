package net.provera.cartserv.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.provera.cartserv.dao.model.CartItem;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEventDto {
    private String userId;
    private List<CartItem> cartItems;
    private String eventType;
}
