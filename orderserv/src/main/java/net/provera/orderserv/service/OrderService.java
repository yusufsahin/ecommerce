package net.provera.orderserv.service;

import net.provera.orderserv.dao.model.Order;

import java.util.List;

public interface OrderService {

    public Order createOrder(Order order);
    public List<Order> getAllOrdersByUserId(String userId);

    List<Order> getAllOrders();

    Order updateOrder(String orderId, Order order);

    void deleteOrder(String id);
}
