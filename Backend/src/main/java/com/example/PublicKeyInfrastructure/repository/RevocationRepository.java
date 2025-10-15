package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.Revocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevocationRepository extends JpaRepository<Revocation, Integer> {
}
