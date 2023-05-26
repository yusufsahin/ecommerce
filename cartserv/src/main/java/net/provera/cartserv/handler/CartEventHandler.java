package net.provera.cartserv.handler;

import net.provera.cartserv.dao.model.CartItem;
import net.provera.cartserv.event.CartEvent;
import net.provera.cartserv.event.CartEventType;
import net.provera.cartserv.service.CartService;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CartEventHandler {
    private final CartService cartService;
    CartEventHandler(CartService cartService){
        this.cartService=cartService;
    }
    public void handleEvent(CartEvent event) {
        List<CartItem> cartItems = event.getCartItems();
        String userId = event.getUserId();
        CartEventType eventType = event.getEventType();

        switch (eventType) {
            case CLEARED:
                cartService.clearCart(userId);
                break;
            case ITEMADDED:
                    cartService.addItem(userId, cartItems);
                break;
            case ITEMREMOVED:
                for (CartItem item:
                        cartItems) {
                    cartService.removeItem(userId, item.getProductId());
                }
                break;
            case ITEMUPDATED:
                for (CartItem item:
                        cartItems) {
                    cartService.updateItem(userId, item);
                }
            default:
                throw new IllegalArgumentException("Invalid event type");
        }
    }
}
