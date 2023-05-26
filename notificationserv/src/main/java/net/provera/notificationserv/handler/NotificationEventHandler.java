package net.provera.notificationserv.handler;


import net.provera.notificationserv.dao.model.Notification;
import net.provera.notificationserv.event.CartEventDto;
import net.provera.notificationserv.event.OrderEventDto;
import net.provera.notificationserv.service.NotificationService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
@Component
public class NotificationEventHandler {
    private final NotificationService notificationService;
    private String userId;
    NotificationEventHandler(NotificationService notificationService){
        this.notificationService=notificationService;
    }
    public void handleCartEvent(CartEventDto cartEventDto) {
        userId = cartEventDto.getUserId();
        Notification notification = new Notification();
        notification.setDate(Date.from(Instant.now()));
        notification.setSender("Cart Service");
        notification.setRecipient(cartEventDto.getUserId());
        notification.setTitle(cartEventDto.getEventType().equals("CLEARED")?"CART CLEARED":
                cartEventDto.getEventType().equals("ITEMADDED")?"CART ITEM ADDED":
                cartEventDto.getEventType().equals("ITEMREMOVED")?"CART ITEM REMOVED":
                cartEventDto.getEventType().equals("ITEMUPDATED")?"CART ITEM UPDATED":"ORDERED");
        notification.setMessage(cartEventDto.getCartItems().toString());
        notification.setRead(false);
        System.out.println(notification.getTitle() + notification.getMessage());
        notificationService.saveNotification(notification);
    }
    public void handleOrderEvent(OrderEventDto orderEventDto) {
        Notification notification = new Notification();
        notification.setDate(Date.from(Instant.now()));
        notification.setSender("Order Service");
        notification.setRecipient(userId);
        notification.setTitle(orderEventDto.getEventType().equals("CREATED")?"ORDER CREATED":
                orderEventDto.getEventType().equals("UPDATED")?"ORDER UPDATED":"ORDER DELETED");
        notification.setMessage(orderEventDto.getOrder().toString());
        notification.setRead(false);
        System.out.println(notification.getTitle()+notification.getMessage());
        notificationService.saveNotification(notification);
    }
}
