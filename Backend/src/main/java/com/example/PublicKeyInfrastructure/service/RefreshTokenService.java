package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.RefreshToken;
import com.example.PublicKeyInfrastructure.repository.AuthenticatedUserRepository;
import com.example.PublicKeyInfrastructure.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    public RefreshToken createRefreshToken(String email){
        AuthenticatedUser foundUser = authenticatedUserService.findUserByEmail(email);
        RefreshToken refreshToken = findRefreshTokenByUser(foundUser);
        if (refreshToken == null) {
            refreshToken = new RefreshToken();
            refreshToken.setUser(foundUser);
        }
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setValid(true);

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken findRefreshTokenByUser(AuthenticatedUser user){
        return refreshTokenRepository.findRefreshTokenByUser(user).orElse(null);
    }

    public void revokeRefreshToken(String email){
        AuthenticatedUser foundUser = authenticatedUserService.findUserByEmail(email);
        RefreshToken refreshToken = findRefreshTokenByUser(foundUser);
        refreshToken.setValid(false);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshTokenByToken(String token){
        return refreshTokenRepository.findByToken(token).orElse(null);
    }
}
