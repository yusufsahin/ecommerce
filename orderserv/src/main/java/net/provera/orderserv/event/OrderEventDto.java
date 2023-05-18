package net.provera.orderserv.event;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.provera.orderserv.dao.model.Order;

import java.io.Serial;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Order order;
    private String eventType;
}
