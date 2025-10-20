package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.AdminMasterKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminMasterKeyRepository extends JpaRepository<AdminMasterKey, Integer> {

    Optional<AdminMasterKey> findById(Integer integer);
}
