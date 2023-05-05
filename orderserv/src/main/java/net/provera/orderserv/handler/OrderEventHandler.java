package net.provera.orderserv.handler;

import net.provera.orderserv.dao.model.Order;
import net.provera.orderserv.event.OrderEvent;
import net.provera.orderserv.event.OrderEventType;
import net.provera.orderserv.service.OrderService;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {

    private final OrderService orderService;

    public OrderEventHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    public void handleEvent(OrderEvent event) {
        Order order = event.getOrder();
        OrderEventType eventType = event.getEventType();

        switch (eventType) {
            case CREATED:
                orderService.createOrder(order);
                break;
            case UPDATED:
                orderService.updateOrder(order.getId(), order);
                break;
            case DELETED:
                orderService.deleteOrder(order.getId());
                break;
            default:
                throw new IllegalArgumentException("Invalid event type");
        }
    }
}
