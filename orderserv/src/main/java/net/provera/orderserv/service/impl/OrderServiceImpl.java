package net.provera.orderserv.service.impl;

import net.provera.orderserv.dao.OrderRepository;
import net.provera.orderserv.dao.model.Order;
import net.provera.orderserv.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(String orderId, Order order) {
        Optional<Order> orderdb = orderRepository.findById(orderId);
        if(orderdb.isPresent()){
            // güncellenecek
            orderdb.get().setProductId(order.getProductId());
            return orderRepository.save(orderdb.get());
        }
        return null;
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getAllOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
