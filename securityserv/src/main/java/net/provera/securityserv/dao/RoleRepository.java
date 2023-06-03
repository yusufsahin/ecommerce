package net.provera.securityserv.dao;

import jakarta.transaction.Transactional;
import net.provera.securityserv.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Transactional
//JaversSpringDataAuditable
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    public Role findByName(String name);
}
