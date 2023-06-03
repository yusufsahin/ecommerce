package net.provera.securityserv.service.impl;

import net.provera.securityserv.dao.RefreshTokenRepository;
import net.provera.securityserv.dao.entity.RefreshToken;
import net.provera.securityserv.dao.entity.User;
import net.provera.securityserv.dto.UserDto;
import net.provera.securityserv.service.RefreshTokenService;
import net.provera.securityserv.util.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final ModelMapper modelMapper;


    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, ModelMapper modelMapper) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean isRefreshExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

    @Override
    public RefreshToken createRefreshToken(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(new Date(System.currentTimeMillis() + Constant.REFRESH_TOKEN_VALIDITY_SECONDS * 1000));
        refreshTokenRepository.save(token);
        return token;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
