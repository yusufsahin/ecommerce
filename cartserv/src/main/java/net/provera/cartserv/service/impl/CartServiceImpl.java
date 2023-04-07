package net.provera.cartserv.service.impl;

import net.provera.cartserv.dao.model.CartItem;
import net.provera.cartserv.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> hashOperations;

    @Autowired
    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    private String getCartKey(String userId) {
        String cartId = (String) hashOperations.get("userCarts", userId);
        if (cartId == null) {
            cartId = UUID.randomUUID().toString();
            hashOperations.put("userCarts", userId, cartId);
        }
        return userId + ":" + cartId;
    }

    @Override
    public void addItem(String userId, CartItem item) {
        String key = getCartKey(userId);
        hashOperations.put(key, item.getId(), item);
    }

    @Override
    public void removeItem(String userId, String itemId) {
        String key = getCartKey(userId);
        hashOperations.delete(key, itemId);
    }

    @Override
    public void updateItem(String userId, CartItem item) {
        String key = getCartKey(userId);
        hashOperations.put(key, item.getId(), item);
    }

    @Override
    public List<CartItem> getCartItems(String userId) {
        String key = getCartKey(userId);
        return hashOperations.entries(key)
                .values()
                .stream()
                .map(cartItem -> (CartItem) cartItem)
                .collect(Collectors.toList());
    }

    @Override
    public void clearCart(String userId) {
        String key = getCartKey(userId);
        redisTemplate.delete(key);
    }
}
