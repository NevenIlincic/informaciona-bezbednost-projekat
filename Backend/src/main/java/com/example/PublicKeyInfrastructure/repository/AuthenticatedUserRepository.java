package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticatedUserRepository extends JpaRepository<AuthenticatedUser, Integer> {
}
