package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String refreshToken);
    Optional<RefreshToken> findRefreshTokenByUser(AuthenticatedUser user);
}
