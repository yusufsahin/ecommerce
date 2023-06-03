package net.provera.securityserv.dao;


import net.provera.securityserv.dao.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    public RefreshToken findByToken(String token);
}

