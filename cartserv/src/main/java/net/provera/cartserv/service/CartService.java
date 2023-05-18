package net.provera.cartserv.service;

import net.provera.cartserv.dao.model.CartItem;

import java.util.List;

public interface CartService {
    void addItem(String userId, List<CartItem> item);
    void removeItem(String userId, String itemId);
    void updateItem(String userId, CartItem item);
    List<CartItem> getCartItems(String userId);
    void clearCart(String userId);
    void order(String userId);
}
