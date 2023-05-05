package net.provera.orderserv.event;


import net.provera.orderserv.dao.model.Order;

import java.io.Serializable;

public class OrderEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Order order;
    private OrderEventType eventType;

    public OrderEvent(Order order, OrderEventType eventType) {
        this.order = order;
        this.eventType = eventType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderEventType getEventType() {
        return eventType;
    }

    public void setEventType(OrderEventType eventType) {
        this.eventType = eventType;
    }
}
