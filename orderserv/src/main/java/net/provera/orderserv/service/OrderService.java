package net.provera.orderserv.service;

import net.provera.orderserv.dao.model.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getAllOrdersByUserId(String userId);
    Order getOrderById(String orderId);

    List<Order> getAllOrders();

    Order updateOrder(String orderId, Order order);

    void deleteOrder(String id);

    void publishOrderEvent(Order order, String routingKey);
}
