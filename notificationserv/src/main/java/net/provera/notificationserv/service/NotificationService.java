package net.provera.notificationserv.service;


import net.provera.notificationserv.dao.model.Notification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NotificationService {

    Notification saveNotification(Notification notification);

    Notification getNotificationById(UUID id);

    List<Notification> getAllNotifications();

    List<Notification> getUnreadNotifications();

    void markNotificationAsRead(UUID id);

    void deleteNotification(UUID id);

}