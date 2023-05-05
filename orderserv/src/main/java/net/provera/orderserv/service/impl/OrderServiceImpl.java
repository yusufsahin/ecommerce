package net.provera.orderserv.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.provera.orderserv.dao.OrderRepository;
import net.provera.orderserv.dao.model.Order;
import net.provera.orderserv.event.OrderEvent;
import net.provera.orderserv.event.OrderEventType;
import net.provera.orderserv.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public OrderServiceImpl(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        publishOrderEvent(savedOrder, "order.created");
        return savedOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrder(String orderId, Order order) {
        Optional<Order> orderDb = orderRepository.findById(orderId);
        if (orderDb.isPresent()) {
            orderDb.get().setStatus(order.getStatus());
            Order updatedOrder = orderRepository.save(orderDb.get());
            publishOrderEvent(updatedOrder, "order.updated");
            return updatedOrder;
        }
        return null;
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
        publishOrderEvent(new Order(id, null, null, 0, null), "order.deleted");
    }

    @Override
    public List<Order> getAllOrdersByUserId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void publishOrderEvent(Order order, String routingKey) {
        try {
            OrderEvent orderEvent = new OrderEvent(order, routingKey.equals("order.created") ? OrderEventType.CREATED :
                    routingKey.equals("order.updated") ? OrderEventType.UPDATED : OrderEventType.DELETED);
            String orderEventJson = objectMapper.writeValueAsString(orderEvent);
            rabbitTemplate.convertAndSend("orders", routingKey, orderEventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to publish order event", e);
        }
    }
}
