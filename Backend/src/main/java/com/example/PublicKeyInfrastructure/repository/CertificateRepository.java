package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {
}
