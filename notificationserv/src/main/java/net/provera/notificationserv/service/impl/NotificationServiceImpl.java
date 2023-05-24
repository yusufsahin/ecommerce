package net.provera.notificationserv.service.impl;

import net.provera.notificationserv.dao.NotificationRepository;
import net.provera.notificationserv.dao.model.Notification;
import net.provera.notificationserv.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification saveNotification(Notification notification) {
        notification.setId(UUID.randomUUID());
        notification.setDate(new Date());
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByReadFalse();
    }

    @Override
    public void markNotificationAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }
}