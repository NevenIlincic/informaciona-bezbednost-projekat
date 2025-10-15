package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser, Integer> {

    Optional<AuthenticatedUser> findByEmail(String email);
    Optional<AuthenticatedUser> findByActivationToken(String token);
}
