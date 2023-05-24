package net.provera.notificationserv.dao;

import net.provera.notificationserv.dao.model.Notification;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends CassandraRepository<Notification, UUID> {
}
