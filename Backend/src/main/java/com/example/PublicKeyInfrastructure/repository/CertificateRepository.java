package com.example.PublicKeyInfrastructure.repository;

import com.example.PublicKeyInfrastructure.model.Certificate;
import com.example.PublicKeyInfrastructure.model.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    @Query("SELECT e FROM Certificate e " +
            "WHERE e.isRevoked = false AND e.type = :certificateType ")
    List<Certificate> returnNonRevokedCertificates(CertificateType certificateType);
}
