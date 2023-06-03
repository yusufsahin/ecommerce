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

@Table(name = "privileges",uniqueConstraints = {
        @UniqueConstraint(name = "UniquePrivilegeName", columnNames = {"name"})})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Where(clause = "is_deleted='false'")
@SQLDelete(sql = "UPDATE privileges SET is_deleted = true WHERE id = ? and version = ?")

@EntityListeners(AuditingEntityListener.class)
public class Privilege extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name",unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy="privileges")
    private Collection<Role> roles;

}
