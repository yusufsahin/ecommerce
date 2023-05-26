package net.provera.notificationserv.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import net.provera.notificationserv.dao.model.Order;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventDto implements Serializable {

    private Order order;
    private String eventType;
}