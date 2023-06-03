package net.provera.securityserv.dao.entity;

import jakarta.persistence.*;
import lombok.*;
import net.provera.securityserv.dao.entity.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("serial")
@Entity
@Builder

//indexes = {@Index(name = "idx_username", columnList = "username")},uniqueConstraints={@UniqueConstraint(columnNames={"username"}),@UniqueConstraint(columnNames={"email"})}
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(name = "UniqueUsername", columnNames = {"username"}),
        @UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Where(clause = "is_deleted='false'")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ? and version = ?")

@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @Column(name = "username",unique = true)
    private String username;
    @NonNull
    @Column(name = "pwd")
    private String password;
    // private byte[] passwordSalt;
    @NonNull
    @Column(name = "email",unique = true)
    private String email;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "phone_num")
    private String phonenum;
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "forgot_password_guid")
    private UUID forgotPasswordGuid;

    @Temporal(TemporalType.DATE)
    @Column(name = "forgot_password_valid_date")
    private Date forgotPasswordValidDate;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Collection<Role> roles;


    public User(Long id, String username, String password, String email, String firstname, String lastname,
                String phonenum) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenum = phonenum;
    }
}
