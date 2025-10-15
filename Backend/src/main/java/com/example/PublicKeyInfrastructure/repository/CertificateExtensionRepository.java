package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.CertificateExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateExtensionRepository extends JpaRepository<CertificateExtension, Integer> {
}
