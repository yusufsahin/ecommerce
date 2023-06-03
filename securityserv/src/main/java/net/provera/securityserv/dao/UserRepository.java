package net.provera.securityserv.dao;

import jakarta.transaction.Transactional;
import net.provera.securityserv.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
@Transactional
//@JaversSpringDataAuditable
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);
    //User findByUsernameOrEmail(String username, String email);
    User findByForgotPasswordGuid(UUID forgotPasswordGuid);

}
