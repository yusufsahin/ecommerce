package net.provera.orderserv.event;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.provera.orderserv.dao.model.Order;

import java.io.Serial;
import java.io.Serializable;
@Data
@NoArgsConstructor
public class OrderEvent implements Serializable {

    @Serial
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
