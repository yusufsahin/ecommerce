package net.provera.cartserv.controller;

import net.provera.cartserv.dao.model.CartItem;
import net.provera.cartserv.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addItem(
            @PathVariable String userId,
            @RequestBody CartItem item) {
        cartService.addItem(userId, item);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable String userId,
            @PathVariable String itemId) {
        cartService.removeItem(userId, itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{userId}/items")
    public ResponseEntity<Void> updateItem(
            @PathVariable String userId,
            @RequestBody CartItem item) {
        cartService.updateItem(userId, item);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(
            @PathVariable String userId) {
        List<CartItem> items = cartService.getCartItems(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(
            @PathVariable String userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
