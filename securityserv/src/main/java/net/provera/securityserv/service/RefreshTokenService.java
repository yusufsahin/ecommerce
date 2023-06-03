package net.provera.securityserv.service;


import net.provera.securityserv.dao.entity.RefreshToken;
import net.provera.securityserv.dto.UserDto;

public interface RefreshTokenService {

    public boolean isRefreshExpired(RefreshToken token);

    public RefreshToken createRefreshToken(UserDto userDto);

    public RefreshToken findByToken(String token);
}
