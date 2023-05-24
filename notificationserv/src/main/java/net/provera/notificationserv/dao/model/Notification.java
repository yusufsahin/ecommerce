package net.provera.notificationserv.dao.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @PrimaryKey
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String title;

    private String message;

    private String sender;

    private String recipient;

    private Date date;

    private Boolean read;

}
