package net.provera.securityserv.dao;

import jakarta.transaction.Transactional;
import net.provera.securityserv.dao.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Transactional
//@JaversSpringDataAuditable
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>, JpaSpecificationExecutor<Privilege> {

    public Privilege findByName(String name);
}

