package net.provera.cartserv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.cartserv.dao.model.CartItem;
import net.provera.cartserv.dao.model.Order;
import net.provera.cartserv.event.CartEvent;
import net.provera.cartserv.event.CartEventType;
import net.provera.cartserv.event.OrderEvent;
import net.provera.cartserv.event.OrderEventType;
import net.provera.cartserv.service.CartService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
private final ObjectMapper objectMapper;
    private final HashOperations<String, String, Object> hashOperations;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CartServiceImpl(RedisTemplate<String, Object> redisTemplate,RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.rabbitTemplate=rabbitTemplate;
        this.objectMapper=objectMapper;
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
    public void addItem(String userId, List<CartItem> items) {
        String key = getCartKey(userId);
        for (CartItem item:items) {
            hashOperations.put(key, item.getProductId(), item);
        }
        publishCartEvent(items,"cart.itemAdded",userId);
    }

    @Override
    public void removeItem(String userId, String itemId) {
        String key = getCartKey(userId);
        List<CartItem> cartItems = new java.util.ArrayList<>(Collections.emptyList());
        CartItem cartItem = (CartItem) hashOperations.get(userId,itemId);
        cartItems.add(cartItem);
        hashOperations.delete(key, itemId);
        publishCartEvent(cartItems,"cart.itemRemoved",userId);
    }

    @Override
    public void updateItem(String userId, CartItem item) {
        String key = getCartKey(userId);
        List<CartItem> cartItems = new java.util.ArrayList<>(Collections.emptyList());
        cartItems.add(item);
        hashOperations.put(key, item.getProductId(), item);
        publishCartEvent(cartItems,"cart.itemUpdated",userId);
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
        List<CartItem> cartItems = getCartItems(userId);
        redisTemplate.delete(key);
        publishCartEvent(cartItems,"cart.cleared",userId);
    }

    @Override
    public void order(String userId) {
        List<CartItem> cartItems = getCartItems(userId);
        publishOrderEvent(cartItems,"order.created",userId);
    }
    public void publishCartEvent(List<CartItem> cartItems, String routingKey, String userId) {
        try {
            CartEvent cartEvent = new CartEvent(userId, cartItems,
            routingKey.equals("cart.itemAdded") ? CartEventType.ITEMADDED :
            routingKey.equals("cart.itemUpdated") ? CartEventType.ITEMUPDATED :
            routingKey.equals("cart.itemRemoved") ? CartEventType.ITEMREMOVED : CartEventType.CLEARED);
            String orderEventJson = objectMapper.writeValueAsString(cartEvent);
            rabbitTemplate.convertAndSend("cart", routingKey, orderEventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to publish cart event", e);
        }
    }
    public void publishOrderEvent(List<CartItem> cartItems, String routingKey,String userId) {
        Order order = new Order();
        order.setCustomerId(userId);
        order.setOrderItems(cartItems);
        order.setStatus("pending");
        order.updateTotalAmount();
        OrderEvent cartToOrderEvent = new OrderEvent(order);
        try {
            String orderEventJson = objectMapper.writeValueAsString(cartToOrderEvent);
            rabbitTemplate.convertAndSend("orders", routingKey, orderEventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to publish order event", e);
        }
    }
}
