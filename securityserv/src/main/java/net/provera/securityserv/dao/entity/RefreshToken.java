package net.provera.securityserv.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.provera.securityserv.dao.entity.common.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@SuppressWarnings("serial")
@Builder

@Entity
@Table(name = "refresh_tokens")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// refresh token için gerekli mi?
@Where(clause = "is_deleted='false'")
@SQLDelete(sql = "UPDATE projects SET is_deleted = true WHERE id = ? and version = ?")

@EntityListeners(AuditingEntityListener.class)
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;


}
