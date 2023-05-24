package net.provera.notificationserv.dao.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import java.util.UUID;

public class Notification {

    @PrimaryKey
    private UUID id;

    private String title;

    private String description;
}
