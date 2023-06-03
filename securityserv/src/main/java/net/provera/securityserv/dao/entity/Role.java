package net.provera.securityserv.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import net.provera.securityserv.dao.entity.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;

@SuppressWarnings("serial")
@Entity
@Builder

@Table(name = "roles",uniqueConstraints = {
        @UniqueConstraint(name = "UniqueRoleName", columnNames = {"name"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Where(clause = "is_deleted='false'")
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id = ? and version = ?")
//@SQLDelete(sql = "UPDATE roles SET isdeleted = true WHERE id = ?")

@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    @Column(name="name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy="roles")
    private Collection<User> users;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "ROLE_PRIVILEGES",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;
}
