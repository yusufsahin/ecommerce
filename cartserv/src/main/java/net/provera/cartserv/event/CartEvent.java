package net.provera.cartserv.event;

import net.provera.cartserv.dao.model.CartItem;

import java.io.Serializable;
import java.util.List;

public class CartEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private List<CartItem> cartItems;
    private CartEventType eventType;
    public CartEvent(String userId, List<CartItem> cartItems, CartEventType eventType){
        this.userId=userId;
        this.cartItems=cartItems;
        this.eventType=eventType;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public CartEventType getEventType() {
        return eventType;
    }

    public void setEventType(CartEventType eventType) {
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
